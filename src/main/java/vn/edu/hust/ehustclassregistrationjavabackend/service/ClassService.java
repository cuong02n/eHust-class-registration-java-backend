package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.AdminClassRegistrationRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.StudentClassRegistrationRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.*;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.ExcelUtil;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("DanglingJavadoc")
@Service
@RequiredArgsConstructor
public class ClassService {

    private static final Logger log = LoggerFactory.getLogger(ClassService.class);
    private final UserClassRepository userClassRepository;
    private final ClassRepository classRepository;
    private final MetadataService metadataService;
    private final ServletRequest httpServletRequest;
    private final CourseService courseService;
    private final UserService userService;

    public Class getClassByIdAndSemester(String id, String semester) {
        return classRepository.findByClassPK(new ClassPK(id, semester));
    }

    public Collection<UserClassRegistration> getStudentRegisted(String email, String semester) {
        return userClassRepository.findAllByEmailAndSemester(email, semester);
    }

    public List<ClassDto> updateClassesByFile(MultipartFile file) throws IOException {
        return createClass(ExcelUtil.getClassDtoRequest(file.getInputStream()));
    }

    @CachePut(value = "classes")
    public List<ClassDto> getClassBySemester(String semester) {
        return classRepository.findAllByClassPK_Semester(semester).stream().map(Class::toClassDto).toList();
    }

    public List<ClassDto> createClass(List<ClassDto> classDtos) {
        User admin = (User) httpServletRequest.getAttribute("user");
        return classRepository.saveAll(classDtos.stream().map(classDto -> classDto.toClassEntity(admin)).toList() // Make entity for update database
        ).stream().map(Class::toClassDto).toList();

    }

    public ClassDto cancelClass(ClassPK classPK) {
        User admin = (User) httpServletRequest.getAttribute("user");
        Class oldClass = classRepository.findByClassPK(classPK);
        if (oldClass == null) {
            return null;
        }
        oldClass.setStatus(Class.Status.CANCEL);
        oldClass.setUserModified(admin);
        classRepository.saveAndFlush(oldClass);
        return oldClass.toClassDto();
    }

    public List<ClassDto> getClassByCourseId(String courseId, String semester, boolean countRegisted) {
        List<ClassDto> result = new Vector<>();
        classRepository.findAllByCourseIdAndClassPK_Semester(courseId, semester).forEach(c -> result.add(c.toClassDto()));
        if (countRegisted) {
            for (ClassDto classDto : result) {
                classDto.setCurrentRegisted(userClassRepository.countRegistedByClassIdAndSemester(classDto.getId(), semester));
            }
        }
        return result;
    }

    /**
     * @param student:  student
     * @param semester: semester
     * @return Có trong thời gian được đăng kí lớp không
     */
    public boolean isOpenForStudentRegisterClass(User student, String semester, List<String> courseIdsRequest) {
        if (metadataService.isFreeClassRegister(semester)) {
            return true;
        }
        Set<String> courseIdsRegisted = courseService.getRegistedCourse(student.getEmail(), semester).stream().map(UserCourseRegistration::getCourseId).collect(Collectors.toSet());

        /**
         * Kiểm tra đã đăng kí hết hay chưa
         */
        if (courseIdsRegisted.containsAll(courseIdsRequest)) {
            /**
             * Đã đăng kí hết -> trong thời gian điều chỉnh hay chính thức đều được
             */
            if (student.getStudentType() == User.StudentType.ELITECH) {
                return metadataService.isElitechOfficialRegisterClass(semester) || metadataService.isElitechUnofficialRegisterClass(semester);

            } else {
                // Standard
                return metadataService.isStandardOfficialRegisterClass(semester) || metadataService.isStandardUnofficialRegisterClass(semester);
            }

        } else {
            /**
             * Chưa đăng kí hết-> phải đợi đăng kí điều chỉnh
             */
            if (student.getStudentType() == User.StudentType.ELITECH) {
                return metadataService.isElitechUnofficialRegisterClass(semester);

            } else {
                return metadataService.isStandardUnofficialRegisterClass(semester);
            }
        }
    }

    private void checkSatisfyConstraintCourse(List<Class> newClassIfMerged) {

        Set<Class> theoryClasses = new HashSet<>();
        Set<Class> exersiseClasses = new HashSet<>();
        Set<Class> theoryExerciseClasses = new HashSet<>();
        Set<Class> experimentClasses = new HashSet<>();

        for (Class c : newClassIfMerged) {
            switch (c.getClassType()) {
                case THEORY -> theoryClasses.add(c);
                case EXERCISE -> exersiseClasses.add(c);
                case EXPERIMENT -> experimentClasses.add(c);
                case THEORY_EXERCISE -> theoryExerciseClasses.add(c);
            }
        }

        /**
         * Xét lớp lý thuyết
         */
        for (Class c : theoryClasses) {
            /**
             * Kiểm tra lớp bài tập
             */
            if (exersiseClasses.stream().noneMatch(aClass -> aClass.getTheoryClassId().equals(c.getClassPK().getId()))) {
                throw new MessageException("Lớp " + c.getClassPK().getId() + ": " + c.getCourseId() + " cần đăng ký lớp bài tập");
            }
            /**
             * Kiểm tra thí nghiệm
             */
            if (c.getCourse().getNeedExperiment()) {
                if (experimentClasses.stream().noneMatch(cl -> cl.getCourseId().equals(c.getCourseId()))) {
                    throw new MessageException("Lớp " + c.getClassPK().getId() + ": " + c.getCourseId() + " cần đăng ký lớp thí nghiệm, thực hành");
                }
            }
            /**
             * kiểm tra trùng lặp (khác lớp nhưng chung học phần)
             */
            if (theoryClasses.stream().anyMatch(cl -> cl.getCourseId().equals(c.getCourseId()) && !cl.getClassPK().getId().equals(c.getClassPK().getId()))) {
                throw new MessageException("Học phần " + c.getCourse().getId() + " không thể có 2 lớp lý thuyết trùng nhau");
            }

        }

        /**
         * Xét lớp lý thuyết + bài tập
         */
        log.info("LT+BT: {}", theoryExerciseClasses);
        for (Class c : theoryExerciseClasses) {
            /**
             * Kiểm tra thí nghiệm
             */
            if (c.getCourse().getNeedExperiment()) {
                if (experimentClasses.stream().noneMatch(cl -> cl.getCourseId().equals(c.getCourseId()))) {
                    throw new MessageException("Lớp " + c.getClassPK().getId() + ": " + c.getCourseId() + " cần đăng ký lớp thí nghiệm, thực hành");
                }
            }

            /**
             * kiểm tra trùng lặp (khác lớp nhưng chung học phần)
             */
            if (theoryExerciseClasses.stream().anyMatch(cl -> cl.getCourseId().equals(c.getCourseId()) && !cl.getClassPK().getId().equals(c.getClassPK().getId()))) {
                throw new MessageException("Học phần " + c.getCourseId() + " không thể có 2 lớp LT+BT trùng nhau");
            }

        }

        /**
         * Xét lớp bài tập
         */
        for (Class c : exersiseClasses) {
            /**
             * kiểm tra trùng lặp (khác lớp nhưng chung học phần)
             */
            if (exersiseClasses.stream().anyMatch(cl -> cl.getCourseId().equals(c.getCourseId()) && !cl.getClassPK().getId().equals(c.getClassPK().getId()))) {
                throw new MessageException("Học phần " + c.getCourseId() + " không thể có 2 lớp bài tập trùng nhau");
            }
            /**
             * Kiểm tra có lớp lý thuyết không
             */
            if (theoryClasses.stream().noneMatch(cl -> cl.getCourseId().equals(c.getCourseId()))) {
                throw new MessageException("Lớp " + c.getClassPK().getId() + ": " + c.getCourseId() + " chưa đăng ký lớp lý thuyết");
            }

            /**
             * Kiểm tra thí nghiệm
             */
            if (c.getCourse().getNeedExperiment()) {
                if (experimentClasses.stream().noneMatch(cl -> cl.getCourseId().equals(c.getCourseId()))) {
                    throw new MessageException("Lớp " + c.getClassPK().getId() + ": " + c.getCourseId() + " cần đăng ký lớp thí nghiệm, thực hành");
                }
            }
        }

        /**
         * Xét lớp thí nghiệm, thực hành
         */
        for (Class c : experimentClasses) {
            /**
             * Lớp thực hành: phải có lớp lý thuyết đi kèm, hoặc là lớp LT+BT, 2 là lớp LT
             */
            if (
                    theoryClasses.stream().noneMatch(cl -> cl.getCourseId().equals(c.getCourseId()))
                            || theoryExerciseClasses.stream().noneMatch(cl -> cl.getCourseId().equals(c.getCourseId()))
            ) {
                throw new MessageException("Lớp thí nghiệm " + c.getClassPK().getId() + ": " + c.getCourseId() + " Chưa đăng kí lớp lý thuyết");
            }

            /**
             * kiểm tra trùng lặp (khác lớp nhưng chung học phần)
             */
            if (experimentClasses.stream().anyMatch(cl -> cl.getCourseId().equals(c.getCourseId()) && !cl.getClassPK().getId().equals(c.getClassPK().getId()))) {
                throw new MessageException("Học phần " + c.getCourseId() + " không thể có 2 lớp thí nghiệm trùng nhau");
            }
        }
    }

    public List<UserClassRegistration> registerClassByStudent(StudentClassRegistrationRequest rq) {
        User student = (User) httpServletRequest.getAttribute("user");

        List<UserClassRegistration> existingRegistrations = userClassRepository.findAllByEmailAndSemester(student.getEmail(), rq.getSemester());
        List<String> existingRegistrationIds = existingRegistrations.stream().map(UserClassRegistration::getClassId).toList();
        List<String> duplicatedClassIds = rq.getClassIds().stream().filter(existingRegistrationIds::contains).toList();

        /**
         * Kiểm tra xem đã có class bị trùng đăng kí hay chưa, nếu đã đăng kí rồi thì lỗi
         * */
        if (!duplicatedClassIds.isEmpty()) {
            throw new MessageException("Bạn đã đăng kí lớp học này rồi " + duplicatedClassIds);
        }


        List<Class> registedClassRequests = classRepository.findAllByClassPK_SemesterAndClassPK_IdIn(rq.getSemester(), rq.getClassIds());

        if (registedClassRequests.stream().map(Class::getCourseId).collect(Collectors.toSet()).size() != registedClassRequests.size()) {
            throw new MessageException("Hãy kiểm tra lại, có học phần trùng nhau");
        }

        /**
         * Kiểm tra xem có trong thời gian đăng kí môn học không
         */
        if (isOpenForStudentRegisterClass(student, rq.getSemester(), registedClassRequests.stream().map(c -> c.getCourse().getId()).toList())) {
            throw new MessageException("Không phải thời gian đăng kí 1 trong các lớp này, có thể là do bạn chưa đăng kí học phần");
        }

        /**
         * Kiểm tra xem đủ học phần LT,BT,TN chưa
         */

        List<Class> newClassRegistedIfSuccess = new Vector<>();

        newClassRegistedIfSuccess.addAll(existingRegistrations.stream().map(UserClassRegistration::getAClass).toList());
        newClassRegistedIfSuccess.addAll(registedClassRequests);
        checkSatisfyConstraintCourse(newClassRegistedIfSuccess);

        /**
         * Kiểm tra xem có quá số lượng tín chỉ ko
         */
        checkClassExceedStudentMaximumCredit(student, newClassRegistedIfSuccess);

        /**
         * Kiểm tra học phần tiên quyết, học phần song hành, học trước
         */

        checkFullSlotClass(registedClassRequests);

        List<UserClassRegistration> registedWillSave = new Vector<>();
        for (Class c : registedClassRequests) {
            UserClassRegistration entity = UserClassRegistration
                    .builder()
                    .classId(c.getClassPK().getId())
                    .semester(c.getClassPK().getSemester())
                    .email(student.getEmail())
                    .build();
            entity.setUserModified(student);
            registedWillSave.add(entity);
        }
        return userClassRepository.saveAllAndFlush(registedWillSave);
    }

    public List<UserClassRegistration> registerClassByAdmin(AdminClassRegistrationRequest rq) {
        User admin = (User) httpServletRequest.getAttribute("user");
        User student = userService.findUserByEmail(rq.getStudentEmail());

        List<UserClassRegistration> existingRegistrations = userClassRepository.findAllByEmailAndSemester(student.getEmail(), rq.getSemester());
        List<String> existingRegistrationIds = existingRegistrations.stream().map(UserClassRegistration::getClassId).toList();
        List<String> duplicatedClassIds = rq.getClassIds().stream().filter(existingRegistrationIds::contains).toList();

        /**
         * Kiểm tra xem đã có class bị trùng đăng kí hay chưa, nếu đã đăng kí rồi thì lỗi
         * */
        if (!duplicatedClassIds.isEmpty()) {
            throw new MessageException("Bạn đã đăng kí lớp học này rồi " + duplicatedClassIds);
        }

        List<Class> registedClassRequests = classRepository.findAllByClassPK_SemesterAndClassPK_IdIn(rq.getSemester(), rq.getClassIds());

        if (registedClassRequests.stream().map(Class::getCourseId).collect(Collectors.toSet()).size() != registedClassRequests.size()) {
            throw new MessageException("Hãy kiểm tra lại, có học phần trùng nhau");
        }

        /**
         * Kiểm tra xem đủ học phần LT,BT,TN chưa
         */
        List<Class> newClassRegistedIfSuccess = new Vector<>();
        newClassRegistedIfSuccess.addAll(existingRegistrations.stream().map(UserClassRegistration::getAClass).toList());
        newClassRegistedIfSuccess.addAll(registedClassRequests);
        checkSatisfyConstraintCourse(newClassRegistedIfSuccess);

        /**
         * Kiểm tra xem có quá số lượng tín chỉ ko
         */
        checkClassExceedStudentMaximumCredit(student, newClassRegistedIfSuccess);


        /**
         * Kiểm tra học phần tiên quyết, học phần song hành, học trước
         */

        /**
         * Không cần kiểm tra lớp đầy: admin đăng kí thì lớp đầy vẫn được
         */

        List<UserClassRegistration> registedWillSave = new Vector<>();
        for (Class c : registedClassRequests) {
            UserClassRegistration entity = UserClassRegistration
                    .builder()
                    .classId(c.getClassPK().getId())
                    .semester(c.getClassPK().getSemester())
                    .email(student.getEmail())
                    .build();
            entity.setUserModified(admin);
            registedWillSave.add(entity);
        }
        return userClassRepository.saveAllAndFlush(registedWillSave);
    }

    public List<UserClassRegistration> unRegisterClassByStudent(StudentClassRegistrationRequest rq) {

        User student = (User) httpServletRequest.getAttribute("user");
        List<UserClassRegistration> existingClassRegistration = userClassRepository.findAllByEmailAndSemester(student.getEmail(), rq.getSemester());
        List<Class> registedClass = existingClassRegistration.stream().map(UserClassRegistration::getAClass).toList();

        // TODO :  kiểm tra xem có phải thời điểm cho phép không

        /**
         * Kiểm tra xem request có hợp lệ hay ko (chỉ xóa những class đã đăng kí)
         */
        if (!registedClass.stream().map(c -> c.getClassPK().getId()).collect(Collectors.toSet()).containsAll(rq.getClassIds())) {
            throw new MessageException("Yêu cầu xóa không hợp lệ, bạn đã yêu cầu xóa lớp chưa đăng kí");
        }

        /**
         * Kiểm tra xem có lớp nào do admin đăng kí không (không phải do mình đăng kí)
         */
        List<String> classRegistedByAdmin = existingClassRegistration.stream().map(BaseEntity::getCreatedById).filter(s -> !s.equals(student.getEmail())).toList();
        if (rq.getClassIds().stream().anyMatch(classRegistedByAdmin::contains)) {
            throw new MessageException("Bạn không thể hủy lớp do quản trị viên đã đăng kí");
        }

        List<Class> newClassIfDeletedSuccess = existingClassRegistration.stream().map(UserClassRegistration::getAClass).filter(e -> rq.getClassIds().contains(e.getClassPK().getId())).toList();

        /**
         * Kiểm tra xem sau khi xóa thì có thỏa mãn điều kiện của các lớp thí nghiệm, thực hành hay ko
         */
        checkSatisfyConstraintCourse(newClassIfDeletedSuccess);

        return userClassRepository.deleteAllBySemesterAndClassIdIn(rq.getSemester(), rq.getClassIds());
    }


    private void checkFullSlotClass(List<Class> registedClasses) {
//        return getNumberOfRegistedClass(registedClass) >= registedClass.getMaxStudent();
        // TODO
    }

    public int getCreditRegisted(User user, String semester) {
        return userClassRepository.sumCreditByEmailAndSemester(user.getEmail(), semester);
    }


    public void checkClassExceedStudentMaximumCredit(User student, List<Class> newClassIfActionSuccess) {
        int totalCredit = newClassIfActionSuccess
                .stream()
                .collect(
                        Collectors.toMap(
                                Class::getCourseId,
                                Function.identity(),
                                (existElement, newElement) -> existElement
                        )
                ).values().stream().mapToInt(c -> c.getCourse().getCredit()).sum();
        if (student.getMaxCredit() < totalCredit)
            throw new MessageException("Bạn không thể đăng kí quá số tín chỉ tối đa cho phép: " + student.getMaxCredit() + " < " + totalCredit);
    }

}

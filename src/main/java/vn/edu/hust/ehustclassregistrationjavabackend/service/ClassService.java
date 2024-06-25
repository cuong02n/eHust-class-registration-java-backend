package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.AdminClassRegisterRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.ChangeClassRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.StudentClassRegisterRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.*;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.ExcelUtil;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.TimetableUtil;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("DanglingJavadoc")
@Service
@RequiredArgsConstructor
public class ClassService {

    private final UserClassRepository userClassRepository;
    private final ClassRepository classRepository;
    private final MetadataService metadataService;
    private final ServletRequest httpServletRequest;
    private final CourseService courseService;
    private final UserService userService;

    public Class getClassByIdAndSemester(String id, String semester) {
        return classRepository.findByClassPK(new ClassPK(id, semester)).orElseThrow();
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
        Class oldClass = classRepository.findByClassPK(classPK).orElseThrow();
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
    public void checkTimeOpenForStudent(User student, String semester, List<String> courseIdsRequest) {
        if (metadataService.isFreeClassRegister(semester)) {
            return;
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
                if (!metadataService.isElitechOfficialRegisterClass(semester) && !metadataService.isElitechUnofficialRegisterClass(semester)) {
                    throw new MessageException("Đây không phải là thời điểm đăng kí của bạn 102");
                }

            } else {
                // Standard
                if (!metadataService.isStandardOfficialRegisterClass(semester) && !metadataService.isStandardUnofficialRegisterClass(semester))
                    throw new MessageException("Đây không phải là thời điểm đăng kí của bạn 108");

            }

        } else {
            /**
             * Chưa đăng kí hết-> phải đợi đăng kí điều chỉnh
             */
            if (student.getStudentType() == User.StudentType.ELITECH) {
                if (!metadataService.isElitechUnofficialRegisterClass(semester))
                    throw new MessageException("Đây không phải là thời điểm đăng kí của bạn 118");


            } else {
                if (!metadataService.isStandardUnofficialRegisterClass(semester))
                    throw new MessageException("Đây không phải là thời điểm đăng kí của bạn 123");
            }
        }
    }


    private void checkSatisfyConstraintCourse(List<Class> newClassIfMerged) {
        // TODO Chưa check trùng học phần
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
         * Xét lớp lý thuyết:
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
             * Lớp thực hành: phải có lớp lý thuyết đi kèm, (hoặc là lớp LT+BT, hoặc là lớp LT)
             */
            if (
                    theoryClasses.stream().noneMatch(cl -> cl.getCourseId().equals(c.getCourseId()))
                            && theoryExerciseClasses.stream().noneMatch(cl -> cl.getCourseId().equals(c.getCourseId()))
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

    public List<UserClassRegistration> registerClassByStudent(StudentClassRegisterRequest rq) {
        User student = (User) httpServletRequest.getAttribute("user");
        /**
         * Tìm class đã đăng ký
         */
        List<UserClassRegistration> existingRegistrations = userClassRepository.findAllByEmailAndSemester(student.getEmail(), rq.getSemester());
        Set<String> existingRegistrationIds = existingRegistrations.stream().map(UserClassRegistration::getClassId).collect(Collectors.toSet());

        List<String> duplicatedClassIds = rq.getClassIds().stream().filter(existingRegistrationIds::contains).toList();
        // TODO :  kiểm tra xem có phải thời điểm cho phép không

        /**
         * Kiểm tra xem đã có class bị trùng đăng kí hay chưa, nếu đã đăng kí rồi thì lỗi
         * */
        if (!duplicatedClassIds.isEmpty()) {
            throw new MessageException("Bạn đã đăng kí lớp học này rồi " + duplicatedClassIds);
        }


        List<Class> registedClassRequests = classRepository.findAllByClassPK_SemesterAndClassPK_IdIn(rq.getSemester(), rq.getClassIds());

        /**
         * Không được đăng ký lớp LT
         */

        for (Class cl : registedClassRequests) {
            if (cl.getTheoryClassId() == null || cl.getTheoryClassId().isEmpty())
                throw new MessageException("Không được đăng ký lớp LT: " + cl.getClassPK().getId());
        }

        /**
         * Kiểm tra xem có trong thời gian đăng kí môn học không
         */
        checkTimeOpenForStudent(student, rq.getSemester(), registedClassRequests.stream().map(c -> c.getCourse().getId()).toList());

        /**
         * TODO: error: Tự động thêm lớp lý thuyết nếu có lớp bài tập
         */
        registedClassRequests.addAll(classRepository.findAllByClassPK_SemesterAndClassPK_IdIn(rq.getSemester(), registedClassRequests.stream().filter(r -> !r.getClassPK().getId().equals(r.getTheoryClassId())).map(Class::getTheoryClassId).toList()));


        /**
         * Kiểm tra xem đủ học phần LT,BT,TN chưa
         */

        List<Class> newClassRegistedIfSuccess = new Vector<>();

        newClassRegistedIfSuccess.addAll(existingRegistrations.stream().map(UserClassRegistration::getAClass).toList());
        newClassRegistedIfSuccess.addAll(registedClassRequests);

        checkSatisfyConstraintCourse(newClassRegistedIfSuccess);

        /**
         * Kiểm tra tkb
         */
        checkTimetable(newClassRegistedIfSuccess);

        /**
         * Kiểm tra xem có quá số lượng tín chỉ ko
         */
        checkClassExceedStudentMaximumCredit(student, newClassRegistedIfSuccess);

        /**
         * TODO: Kiểm tra học phần tiên quyết, học phần song hành, học trước
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

    public List<UserClassRegistration> registerClassByAdmin(AdminClassRegisterRequest rq) {
        User admin = (User) httpServletRequest.getAttribute("user");
        User student = userService.findUserByEmail(rq.getStudentEmail());
        /**
         * Tìm class đã đăng ký
         */
        List<UserClassRegistration> existingRegistrations = userClassRepository.findAllByEmailAndSemester(student.getEmail(), rq.getSemester());
        Set<String> existingRegistrationIds = existingRegistrations.stream().map(UserClassRegistration::getClassId).collect(Collectors.toSet());

        List<String> duplicatedClassIds = rq.getClassIds().stream().filter(existingRegistrationIds::contains).toList();
        // TODO :  kiểm tra xem có phải thời điểm cho phép không

        if (!duplicatedClassIds.isEmpty()) {
            throw new MessageException("Sinh viên đã đăng kí lớp học này rồi " + duplicatedClassIds);
        }
        /**
         * Không cần check thời gian đăng kí hp
         */
        List<Class> registedClassRequests = classRepository.findAllByClassPK_SemesterAndClassPK_IdIn(rq.getSemester(), rq.getClassIds());


        /**
         * Không được đăng ký lớp LT
         */

        for (Class cl : registedClassRequests) {
            if (cl.getTheoryClassId() == null || cl.getTheoryClassId().isEmpty())
                throw new MessageException("Không được đăng ký lớp LT: " + cl.getClassPK().getId());
        }

        /**
         * TODO: error: Tự động thêm lớp lý thuyết nếu có lớp bài tập, data từ excel có thể lỗi
         */
        registedClassRequests.addAll(classRepository.findAllByClassPK_SemesterAndClassPK_IdIn(rq.getSemester(), registedClassRequests.stream().filter(r -> !r.getClassPK().getId().equals(r.getTheoryClassId())).map(Class::getTheoryClassId).toList()));


        /**
         * Kiểm tra xem đủ học phần LT,BT,TN chưa
         */

        List<Class> newClassRegistedIfSuccess = new Vector<>();

        newClassRegistedIfSuccess.addAll(existingRegistrations.stream().map(UserClassRegistration::getAClass).toList());
        newClassRegistedIfSuccess.addAll(registedClassRequests);

        checkSatisfyConstraintCourse(newClassRegistedIfSuccess);

        /**
         * Kiểm tra tkb
         */
        checkTimetable(newClassRegistedIfSuccess);


        /**
         * Kiểm tra xem có quá số lượng tín chỉ ko
         */
        checkClassExceedStudentMaximumCredit(student, newClassRegistedIfSuccess);

        /**
         * TODO: Kiểm tra học phần tiên quyết, học phần song hành, học trước
         */

        /**
         * Không cần check lớp đầy
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
        System.out.println(registedWillSave);

        return userClassRepository.saveAllAndFlush(registedWillSave);
    }

    public List<UserClassRegistration> unRegisterClassByStudent(StudentClassRegisterRequest rq) {

        User student = (User) httpServletRequest.getAttribute("user");
        /**
         * Tìm class đã đăng ký
         */
        List<UserClassRegistration> existingClassRegistration = userClassRepository.findAllByEmailAndSemester(student.getEmail(), rq.getSemester());
        List<Class> registedClass = existingClassRegistration.stream().map(UserClassRegistration::getAClass).toList();

        // TODO:  kiểm tra xem có phải thời điểm cho phép không


        /**
         * Kiểm tra xem request có hợp lệ hay ko (chỉ xóa những class đã đăng kí)
         */
        if (!registedClass.stream().map(c -> c.getClassPK().getId()).collect(Collectors.toSet()).containsAll(rq.getClassIds())) {
            throw new MessageException("Yêu cầu xóa không hợp lệ, bạn đã yêu cầu xóa lớp chưa đăng kí");
        }

        /**
         * Xóa lớp -> không cần kiểm tra lớp đầy
         */

        /**
         * Kiểm tra xem có lớp nào do admin đăng kí không (không phải do mình đăng kí)
         */
        List<String> classRegistedByAdmin = existingClassRegistration.stream().map(BaseEntity::getCreatedById).filter(s -> !s.equals(student.getEmail())).toList();
        if (rq.getClassIds().stream().anyMatch(classRegistedByAdmin::contains)) {
            throw new MessageException("Bạn không thể hủy lớp do quản trị viên đã đăng kí");
        }

        /**
         * Các lớp sẽ bị xóa (chưa bao gồm lớp LT)
         */
        List<Class> classWillBeUnregisted = registedClass.stream().filter(c -> rq.getClassIds().contains(c.getClassPK().getId())).toList();
        /**
         * Không được tự xóa lớp LT
         */

        for (Class cl : classWillBeUnregisted) {
            if (cl.getTheoryClassId() == null || cl.getTheoryClassId().isEmpty())
                throw new MessageException("Không được đăng ký lớp LT: " + cl.getClassPK().getId());
        }

        List<String> theoryClassIdWillBeUnregisted = classWillBeUnregisted.stream().filter(c -> !c.getClassPK().getId().equals(c.getTheoryClassId())).map(Class::getTheoryClassId).toList();
        /**
         * Xóa thêm lớp LT nếu có mã lớp kèm khác với mã lớp
         */
        List<String> allClassIdWillBeUnregisted = new ArrayList<>();
        allClassIdWillBeUnregisted.addAll(classWillBeUnregisted.stream().map(c -> c.getClassPK().getId()).toList());
        allClassIdWillBeUnregisted.addAll(theoryClassIdWillBeUnregisted);
        /**
         * Các lớp sau khi xóa, giả sử xóa thành công
         */
        List<Class> newClassIfDeletedSuccess = registedClass.stream().filter(c -> !allClassIdWillBeUnregisted.contains(c.getClassPK().getId())).toList();
        /**
         * Kiểm tra xem sau khi xóa thì có thỏa mãn điều kiện của các lớp thí nghiệm, thực hành hay ko
         */
        checkSatisfyConstraintCourse(newClassIfDeletedSuccess);

        return userClassRepository.deleteAllBySemesterAndClassIdIn(rq.getSemester(), allClassIdWillBeUnregisted);
    }

    public List<UserClassRegistration> unRegisterClassByAdmin(AdminClassRegisterRequest rq) {
        User admin = (User) httpServletRequest.getAttribute("user");
        User student = userService.findUserByEmail(rq.getStudentEmail());

        /**
         * Tìm class đã đăng ký
         */
        List<UserClassRegistration> existingClassRegistration = userClassRepository.findAllByEmailAndSemester(student.getEmail(), rq.getSemester());
        List<Class> registedClass = existingClassRegistration.stream().map(UserClassRegistration::getAClass).toList();

        // Ko cần kiểm tra xem có phải thời điểm cho phép không

        /**
         * Kiểm tra xem request có hợp lệ hay ko (chỉ xóa những class đã đăng kí)
         */
        if (!registedClass.stream().map(c -> c.getClassPK().getId()).collect(Collectors.toSet()).containsAll(rq.getClassIds())) {
            throw new MessageException("Yêu cầu xóa không hợp lệ, bạn đã yêu cầu xóa lớp chưa đăng kí");
        }

        /**
         * Xóa lớp -> không cần kiểm tra lớp đầy
         */

        /**
         * Ko cần kiểm tra xem có lớp nào do admin đăng kí
         */
        /**
         * Các lớp sẽ bị xóa (chưa bao gồm lớp LT)
         */
        List<Class> classWillBeUnregisted = registedClass.stream().filter(c -> rq.getClassIds().contains(c.getClassPK().getId())).toList();

        /**
         * Không được tự xóa lớp LT
         */
        for (Class cl : classWillBeUnregisted) {
            if (cl.getTheoryClassId() == null || cl.getTheoryClassId().isEmpty())
                throw new MessageException("Không được đăng ký lớp LT: " + cl.getClassPK().getId());
        }
        List<String> theoryClassIdWillBeUnregisted = classWillBeUnregisted.stream().filter(c -> !c.getClassPK().getId().equals(c.getTheoryClassId())).map(Class::getTheoryClassId).toList();
        /**
         * Xóa thêm lớp LT nếu có mã lớp kèm khác với mã lớp
         */

        List<String> allClassIdWillBeUnregisted = new ArrayList<>();
        allClassIdWillBeUnregisted.addAll(classWillBeUnregisted.stream().map(c -> c.getClassPK().getId()).toList());
        allClassIdWillBeUnregisted.addAll(theoryClassIdWillBeUnregisted);
        /**
         * Các lớp sau khi xóa, giả sử xóa thành công
         */
        List<Class> newClassIfDeletedSuccess = registedClass.stream().filter(c -> !allClassIdWillBeUnregisted.contains(c.getClassPK().getId())).toList();
        /**
         * Kiểm tra xem sau khi xóa thì có thỏa mãn điều kiện của các lớp thí nghiệm, thực hành hay ko
         */
        checkSatisfyConstraintCourse(newClassIfDeletedSuccess);

        return userClassRepository.deleteAllBySemesterAndClassIdIn(rq.getSemester(), allClassIdWillBeUnregisted);
    }

    private void checkFullSlotClass(List<Class> registedClasses) {
        // TODO:  kiểm tra lớp đã đầy chưa
    }

    public int getCreditRegisted(User user, String semester) {
        return userClassRepository.sumCreditByEmailAndSemester(user.getEmail(), semester);
    }

    private void checkTimetable(List<Class> registedClasses) {
        TimetableUtil.checkValidTimetableClass(registedClasses);
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

    public List<UserClassRegistration> changeToSimilarClass(ChangeClassRequest rq) {
        User student = (User) httpServletRequest.getAttribute("user");
        // TODO: kiểm tra thỏa mãn thời điểm đăng ký
        List<UserClassRegistration> existingClassRegistration = userClassRepository.findAllByEmailAndSemester(student.getEmail(), rq.getSemester());
        List<Class> registedClass = existingClassRegistration.stream().map(UserClassRegistration::getAClass).collect(Collectors.toCollection(ArrayList::new));
        List<Class> tmpOldClass = registedClass.stream().filter(c -> c.getClassPK().getId().equals(rq.getOldClassId())).toList();
        if (tmpOldClass.isEmpty()) {
            throw new MessageException("Không có lớp này " + rq.getOldClassId());
        }
        Class oldClass = tmpOldClass.get(0);
        Class newClass = classRepository.findByClassPK(new ClassPK(rq.getNewClassId(), rq.getSemester())).orElseThrow();

        if (!newClass.getClassType().equals(oldClass.getClassType())) {
            throw new MessageException("2 lớp phải có cùng loại (LT,BT,TH)");
        }
        if (!newClass.getCourseId().equals(oldClass.getCourseId())) {
            throw new MessageException("2 lớp phải có cùng mã học phần");
        }
        /**
         * TODO: Kiểm tra xem có lớp nào do admin đăng kí không (không phải do mình đăng kí)
         */

        if (userClassRepository.countRegistedByClassIdAndSemester(rq.getNewClassId(), rq.getSemester()) >= newClass.getMaxStudent())
            throw new MessageException("Lớp đã đầy: " + rq.getNewClassId());

        // TODO: Kiem tra tkb cho case này
        if (newClass.getClassType() == Class.ClassType.THEORY_EXERCISE || newClass.getClassType() == Class.ClassType.EXPERIMENT) {
            /**
             * LT+BT
             * TN: same
             */
            registedClass.set(registedClass.indexOf(oldClass), newClass);


            /**
             * Thay đổi data ->
             */
            for (UserClassRegistration registration : existingClassRegistration) {
                if (registration.getClassId().equals(oldClass.getClassPK().getId())) {
                    registration.setUserModified(student);
                    registration.setClassId(rq.getNewClassId());
                    return List.of(userClassRepository.saveAndFlush(registration));
                }
            }
            throw new MessageException("lỗi xử lý server, không tìm thấy lớp trong danh sách đã đăng kí: " + rq.getOldClassId());
        } else if (newClass.getClassType() == Class.ClassType.EXERCISE) {
            /**
             * Lớp BT
             */
            registedClass.set(registedClass.indexOf(oldClass), newClass);
            /**
             * Tìm lớp lý thuyết và thay đổi trong list
             */
            Class newTheoryClass = classRepository.findByClassPK(new ClassPK(newClass.getTheoryClassId(), rq.getSemester())).orElseThrow();
            int i;
            for (i = 0; i < registedClass.size(); i++) {
                /**
                 * Tìm thấy lớp LT (chắc chắn)
                 */
                if (registedClass.get(i).getClassPK().getId().equals(newClass.getTheoryClassId())) {
                    registedClass.set(i, newTheoryClass);
                    break;
                }
            }
            if (i == registedClass.size())
                throw new MessageException("lỗi xử lý server, không tìm thấy lớp lý thuyết trong danh sách đã đăng kí: " + rq.getOldClassId(), HttpStatus.INTERNAL_SERVER_ERROR);
            /**
             * Save data cho cả lớp LT + BT
             */
            List<UserClassRegistration> listRegistrationToSave = new ArrayList<>();
            for (UserClassRegistration registration : existingClassRegistration) {
                /**
                 * Save lớp LT mới
                 */
                if (registration.getClassId().equals(newTheoryClass.getClassPK().getId())) {
                    registration.setUserModified(student);
                    registration.setClassId(newTheoryClass.getClassPK().getId());
                    listRegistrationToSave.add(registration);
                }
                /**
                 * Save lớp BT mới
                 */
                if (registration.getClassId().equals(newClass.getClassPK().getId())) {
                    registration.setUserModified(student);
                    registration.setClassId(newClass.getClassPK().getId());
                    listRegistrationToSave.add(registration);
                }
            }
            return userClassRepository.saveAllAndFlush(listRegistrationToSave);
        }
        /**
         * LT: false
         */
        throw new MessageException("Bạn không thể thao tác với lớp lý thuyết");
    }
}

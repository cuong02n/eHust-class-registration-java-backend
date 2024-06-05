package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;
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
        return classRepository.findByClassPK(new ClassPK(id, semester));
    }

    public Collection<UserClassRegistration> getStudentRegisted(String email, String semester) {
        return userClassRepository.findAllByEmailAndSemester(email, semester);
    }

    public List<ClassDto> updateClassesByFile(MultipartFile file) throws IOException {
        return createClass(ExcelUtil.getClassDtoRequest(file.getInputStream()));
    }

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

    private boolean satisfyConstraintCourse() {
        return true;
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

        if(registedClassRequests.stream().map(Class::getCourseId).collect(Collectors.toSet()).size()!=registedClassRequests.size()){
            throw new MessageException("Hãy kiểm tra lại, có học phần trùng nhau");
        }

        /**
         * Kiểm tra xem có trong thời gian đăng kí môn học không
         */
        if (isOpenForStudentRegisterClass(student, rq.getSemester(), registedClassRequests.stream().map(c -> c.getCourse().getId()).toList())) {
            throw new MessageException("Không phải thời gian đăng kí 1 trong các lớp này, có thể là do bạn chưa đăng kí học phần");
        }
        /**
         * Kiểm tra xem có quá số lượng tín chỉ ko
         */
        if (!classNotExceedMaximumCredit(student, rq.getSemester(), registedClassRequests)) {
            throw new MessageException("Quá số lượng tín chỉ cho phép");
        }

        /**
         * Kiểm tra xem đủ học phần LT,BT,TN chưa
         */
        if (!satisfyConstraintCourse()) {
            throw new MessageException("Hãy kiểm tra học phần thí nghiệm, bài tập");
        }
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

        if(registedClassRequests.stream().map(Class::getCourseId).collect(Collectors.toSet()).size()!=registedClassRequests.size()){
            throw new MessageException("Hãy kiểm tra lại, có học phần trùng nhau");
        }
        /**
         * Kiểm tra xem có quá số lượng tín chỉ ko
         */
        if (!classNotExceedMaximumCredit(student, rq.getSemester(), registedClassRequests)) {
            throw new MessageException("Quá số lượng tín chỉ cho phép");
        }

        /**
         * Kiểm tra xem đủ học phần LT,BT,TN chưa
         */
        if (!satisfyConstraintCourse()) {
            throw new MessageException("Hãy kiểm tra học phần thí nghiệm, bài tập");
        }
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
            entity.setUserModified(admin);
            registedWillSave.add(entity);
        }
        return userClassRepository.saveAllAndFlush(registedWillSave);
    }

    public List<UserClassRegistration> unRegisterClassByStudent(StudentClassRegistrationRequest rq) {
        //
        User student = (User) httpServletRequest.getAttribute("user");
        List<UserClassRegistration> existingClassRegistration = userClassRepository.findByEmailAndSemesterAndClassIdIn(student.getEmail(), rq.getSemester(), rq.getClassIds());
        List<String> existingClassIds = existingClassRegistration.stream().map(UserClassRegistration::getClassId).toList();
        if (existingClassRegistration.size() != rq.getClassIds().size()) {
            throw new MessageException("Bạn chưa đăng kí lớp này: " + rq.getClassIds() + ", " + existingClassIds);
        }

        /**
         * Kiểm tra xem có lớp nào do admin đăng kí không (không phải do mình đăng kí)
         */
        List<String> classRegistedByAdmin = existingClassRegistration.stream().map(BaseEntity::getCreatedById).filter(s -> !s.equals(student.getEmail())).toList();
        if (rq.getClassIds().stream().anyMatch(classRegistedByAdmin::contains)) {
            throw new MessageException("Bạn không thể hủy lớp do quản trị viên đã đăng kí");
        }

        // TODO
        return null;
    }


    private void checkFullSlotClass(List<Class> registedClasses) {
//        return getNumberOfRegistedClass(registedClass) >= registedClass.getMaxStudent();
        // TODO
    }

    public int getCreditRegisted(User user, String semester) {
        return userClassRepository.sumCreditByEmailAndSemester(user.getEmail(), semester);
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean classNotExceedMaximumCredit(User student, String semester, List<Class> registerClasses) {
        return getCreditRegisted(student, semester) + registerClasses.stream().mapToInt(cl -> cl.getCourse().getCredit()).sum() <= student.getMaxCredit();
    }

}

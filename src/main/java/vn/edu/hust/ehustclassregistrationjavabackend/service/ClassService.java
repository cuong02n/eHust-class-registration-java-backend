package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.StudentClassRegisterRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserClassRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.ExcelUtil;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final UserClassRepository userClassRepository;
    private final ClassRepository classRepository;
    private final MetadataService metadataService;
    private final ServletRequest httpServletRequest;
    private final CourseService courseService;

    public Class getClassByIdAndSemester(String id, String semester) {
        return classRepository.findByClassPK(new ClassPK(id, semester));
    }

    public List<UserClassRegistration> getRegistedClassByEmailAndSemester(String email,String semester){
        return userClassRepository.findAllByEmailAndSemester(email,semester);
    }

    public List<ClassDto> updateClasses(MultipartFile file) throws IOException {
        return createClass(ExcelUtil.getClassDtoRequest(file.getInputStream()));
    }

    public List<ClassDto> getClassBySemester(String semester) {
        return classRepository.findAllByClassPK_Semester(semester).stream().map(Class::toClassDto).toList();
    }

    public List<ClassDto> createClass(List<ClassDto> classDtos) {
        return classRepository.saveAll(
                        classDtos.stream().map(ClassDto::toClassEntity).toList() // Make entity for update database
                )
                .stream().map(Class::toClassDto).toList();

    }

    public ClassDto cancelClass(ClassPK classPK) {
        Class oldClass = classRepository.findByClassPK(classPK);
        if (oldClass == null) {
            return null;
        }
        oldClass.setStatus(Class.Status.CANCEL);
        classRepository.saveAndFlush(oldClass);
        return oldClass.toClassDto();
    }

    public List<ClassDto> getClassByCourseId(String courseId, String semester,boolean countRegisted) {
        List<ClassDto> result = new Vector<>();
        classRepository.findAllByCourseIdAndClassPK_Semester(courseId, semester).forEach(c -> result.add(c.toClassDto()));
        if(countRegisted){
            for(ClassDto classDto: result){
                classDto.setCurrentRegisted(userClassRepository.countRegistedByClassIdAndSemester(classDto.getId(),semester));
            }
        }
        return result;
    }

    public UserClassRegistration registerClass(ClassPK classPK) {
        User user = (User) httpServletRequest.getAttribute("user");
        var existingRegistration = userClassRepository.findByEmailAndClassIdAndSemester(user.getEmail(), classPK.getId(), classPK.getSemester());
        if (existingRegistration.isPresent()) {
            throw new MessageException("Bạn đã đăng kí lớp nay rồi: " + existingRegistration.get().getClassId());
        }
        Class registedClass = classRepository.findByClassPK(classPK);
        checkClassOpenForStudent(user, registedClass);
        return insertUserClassRegistration(user, registedClass);
    }

    private UserClassRegistration insertUserClassRegistration(User student, Class registedClass) {
        return userClassRepository.saveAndFlush(
                UserClassRegistration.builder()
                        .classId(registedClass.getClassPK().getId())
                        .email(student.getEmail())
                        .semester(registedClass.getClassPK().getSemester())
                        .build()
        );
    }

    private int getNumberOfRegistedClass(Class registedClass) {
        return userClassRepository.countRegistedByClassIdAndSemester(registedClass.getClassPK().getId(), registedClass.getClassPK().getSemester());
    }

    private boolean isFullSlotClass(Class registedClass) {
        return getNumberOfRegistedClass(registedClass) >= registedClass.getMaxStudent();
    }

    public int getCreditRegisted(User user, String semester) {
        return userClassRepository.sumCreditByEmailAndSemester(user.getEmail(), semester);
    }

    public boolean classNotExceedMaximumCredit(User student, String semester, Class registerClass) {
        return getCreditRegisted(student, semester) + registerClass.getCourse().getCredit() <= student.getMaxCredit();
    }

    public void checkClassOpenForStudent(User student, Class registerClass) {
        String semester = registerClass.getClassPK().getSemester();
        if (!classNotExceedMaximumCredit(student, semester, registerClass)) {
            throw new MessageException("Bạn không thể đăng ký số tín chỉ vượt quá giới hạn của bạn");
        }

        if (isFullSlotClass(registerClass)) {
            throw new MessageException("Lớp đã đầy, đợi 1 xíu hoặc liên hệ admin để đăng ký");
        }

        if (!courseService.studentMatchedAllCourseBefore(student, registerClass)) {
            throw new MessageException("Muốn đăng kí học phần này, bạn cần học phần tiên quyết | điều kiện | song hành");
        }

        if (metadataService.isFreeClassRegister(semester)) {
            return;
        }

        if (courseService.isStudentRegisterCourseBefore(student, registerClass.getCourseId(), semester)) {// đã đăng kí học phần rồi->
            if (student.getStudentType() == User.StudentType.ELITECH) {
                if ((metadataService.isElitechOfficialRegisterClass(semester)
                        || metadataService.isElitechUnofficialRegisterClass(semester)))
                    throw new MessageException("Đây không phải thời điểm đăng ký");
            }
            if (student.getStudentType() == User.StudentType.STANDARD) {
                if ((metadataService.isStandardOfficialRegisterClass(semester)
                        || metadataService.isStandardUnofficialRegisterClass(semester)))
                    throw new MessageException("Đây không phải thời điểm đăng ký");
            }
        } else {
            if (student.getStudentType() == User.StudentType.ELITECH) { // chưa đăng kí học phần
                if (metadataService.isElitechUnofficialRegisterClass(semester))
                    throw new MessageException("Đây không phải thời điểm đăng ký");

            }
            if (student.getStudentType() == User.StudentType.STANDARD) {
                if (metadataService.isStandardUnofficialRegisterClass(semester))
                    throw new MessageException("Đây không phải thời điểm đăng ký");
            }
        }
    }
}

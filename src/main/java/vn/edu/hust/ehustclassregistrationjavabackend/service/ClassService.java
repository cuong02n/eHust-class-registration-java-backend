package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
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
    private final HttpServletResponse response;
    private final MetadataService metadataService;
    private final ServletRequest httpServletRequest;

    public Class getClassByIdAndSemester(String id, String semester) {
        return classRepository.findByClassPK(new ClassPK(id, semester));
    }

    public List<ClassDto> updateClasses(MultipartFile file) throws IOException {
        return createClass(ExcelUtil.getClassDtoRequest(file.getInputStream()));
    }

    public List<ClassDto> createClass(List<ClassDto> classDtos) {
        try {
            List<ClassDto> response = classRepository.saveAll(
                            classDtos.stream().map(ClassDto::toClassEntity).toList() // Make entity for update database
                    )
                    .stream().map(Class::toClassDto).toList();
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot update in database because there is 1 or more Course Id not present, insert course first");
        }// make Dto for reponse
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

    public List<ClassDto> getClassByCourseId(String courseId, String semester) {
        List<ClassDto> result = new Vector<>();
        classRepository.findAllByCourseIdAndClassPK_Semester(courseId, semester).forEach(c -> result.add(c.toClassDto()));
        return result;
    }

    public UserClassRegistration registerClass(ClassPK classPK) {
        User user = (User) httpServletRequest.getAttribute("user");
        var existingRegistration = userClassRepository.findByUserIdAndClassIdAndSemester(user.getId(), classPK.getId(), classPK.getSemester());
        if (existingRegistration.isPresent()) {
            throw new RuntimeException("Bạn đã đăng kí lớp nay rồi " + existingRegistration.get());
        }
        Class registedClass = classRepository.findByClassPK(classPK);
        String statusClass = metadataService.isClassOpenForStudent(user,registedClass);
        if (statusClass.isEmpty()) {
            return insertUserClassRegistration(user, registedClass);
        }
        throw new RuntimeException(statusClass);
    }

    private UserClassRegistration insertUserClassRegistration(User student, Class registedClass) {
        return userClassRepository.saveAndFlush(
                UserClassRegistration.builder()
                        .classId(registedClass.getClassPK().getId())
                        .userId(student.getId())
                        .semester(registedClass.getClassPK().getSemester())
                        .build()
        );
    }
}

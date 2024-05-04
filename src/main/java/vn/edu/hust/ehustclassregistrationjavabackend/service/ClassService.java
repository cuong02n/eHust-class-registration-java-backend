package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.TimetableUtil;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;
    private final HttpServletResponse response;

    public Class getClassById(Long id) {
        return classRepository.findById(id).orElse(null);
    }

    public Class getClassByIdAndSemester(String id,String semester){
        return classRepository.findByClassPK(new ClassPK(id,semester));
    }

    public Class createClass(ClassRequest classRequest) {
        if (classRepository.existsByClassPK(new ClassPK(classRequest.getId(), classRequest.getSemester()))) {
            // cannot create class
            return null;
        }

        Class newClass = Class.builder()
                .classPK(new ClassPK(classRequest.getId(), classRequest.getSemester()))
                .semesterType(classRequest.getSemesterType())
                .maxStudent(classRequest.getMaxStudent())
                .timetable(TimetableUtil.toString(classRequest.getTimetable()))
                .courseId(classRequest.getCourseId())
                .status(classRequest.getStatus() != null ? classRequest.getStatus() : Class.Status.OPEN)
                .build();

        return classRepository.save(newClass);
    }
}

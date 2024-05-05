package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.TimetableUtil;

import java.util.List;

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

    public ClassDto createClass(ClassDto classDTO) {
        if (classRepository.existsByClassPK(new ClassPK(classDTO.getId(), classDTO.getSemester()))) {
            return null;
        }

        Class newClass = Class.builder()
                .classPK(new ClassPK(classDTO.getId(), classDTO.getSemester()))
                .semesterType(classDTO.getSemesterType())
                .maxStudent(classDTO.getMaxStudent())
                .timetable(TimetableUtil.toString(classDTO.getTimetable()))
                .courseId(classDTO.getCourseId())
                .status(classDTO.getStatus() != null ? classDTO.getStatus() : Class.Status.OPEN)
                .build();

        var ret=  classRepository.save(newClass).toClassDto();
        System.out.println(GsonUtil.gsonExpose.toJson(ret));
        return ret;
    }

    public List<Class> findClassByCourseId(String courseId) {
        List<Class> classes = classRepository.findAllByCourseId(courseId);
        return classes;
    }
}

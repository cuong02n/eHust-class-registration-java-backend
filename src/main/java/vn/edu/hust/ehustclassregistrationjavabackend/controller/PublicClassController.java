package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/classes")
public class PublicClassController {
    final ClassService classService;

    @GetMapping()
    public ResponseEntity<?> getClass(@RequestParam String id, @RequestParam String semester) {
        return BaseResponse.ok(classService.getClassByIdAndSemester(id, semester).toClassDto(), "Not found class");
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getClasses(@RequestParam String semester){
        return BaseResponse.ok(classService.getClassBySemester(semester));
    }

    @GetMapping("/get-by-course-id")
    public ResponseEntity<?> getClassByCourseId(@RequestParam String courseId, @RequestParam String semester) {
        return BaseResponse.created(classService.getClassByCourseId(courseId, semester,false));
    }

    @GetMapping("/get-by-semester")
    public ResponseEntity<?> getClassBySemester(@RequestParam String semester) {
        return BaseResponse.ok(classService.getClassBySemester(semester));
    }
}

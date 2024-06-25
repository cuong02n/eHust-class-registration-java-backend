package vn.edu.hust.ehustclassregistrationjavabackend.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.StudentCourseRegistrationRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

@RestController
@RequestMapping("/students/courses")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT')")
public class CourseController {
    private final CourseService courseService;



    @GetMapping("/register-courses")
    public ResponseEntity<?> getRegistedCourse(@RequestParam String semester) {
        return BaseResponse.ok(courseService.getRegistedCourse(semester));
    }

    @PostMapping("/register-courses")
    public ResponseEntity<?> registerCourse(@RequestBody StudentCourseRegistrationRequest request) {
        return BaseResponse.created(courseService.registerCourse(request));
    }

    @DeleteMapping("/register-courses")
    public ResponseEntity<?> unregisterCourse(@RequestBody StudentCourseRegistrationRequest request) {
        return BaseResponse.deleted(courseService.unregisterCourse(request.getCourseIds()) + " course(s) unregisted.");
    }
}

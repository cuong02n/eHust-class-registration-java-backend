package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.AdminClassRegisterRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/students")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminStudentController {
    final ClassService classService;
    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/get-class-student-registed")
    public ResponseEntity<?> getClassStudentRegisted(@RequestParam String semester, @RequestParam String studentEmail) {
        return BaseResponse.ok(classService.getStudentRegisted(studentEmail, semester));
    }

    @GetMapping("/get-course-student-registed")
    public ResponseEntity<?> getCourseStudentRegisted(@RequestParam String semester, @RequestParam String studentEmail) {
        return BaseResponse.ok(courseService.getRegistedCourse(studentEmail, semester));
    }

    @PostMapping("/register-by-admin")
    public ResponseEntity<?> registerClassForStudent(@RequestBody @Valid AdminClassRegisterRequest rq) {
        return BaseResponse.ok(classService.registerClassByAdmin(rq));
    }

    @GetMapping("/get-student-info")
    public ResponseEntity<?> getStudentInfo(@RequestParam @Size(min = 8, max = 8) String studentId) {
        return BaseResponse.ok(userService.findStudentById(studentId));
    }

    @DeleteMapping("/un-register-by-admin")
    public ResponseEntity<?> unRegisterClass(@RequestBody @Valid AdminClassRegisterRequest rq) {
        return BaseResponse.ok(classService.unRegisterClassByAdmin(rq));
    }


}

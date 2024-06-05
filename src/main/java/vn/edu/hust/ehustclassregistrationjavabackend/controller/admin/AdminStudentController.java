package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.common.ClassRegisterRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/students")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminStudentController {
    final ClassService classService;
    private final CourseService courseService;

    @GetMapping("/get-class-student-registed")
    public ResponseEntity<?> getClassStudentRegisted(@RequestParam String semester, @RequestParam String studentEmail){
        return BaseResponse.ok(classService.getStudentRegisted(studentEmail,semester));
    }

    @GetMapping("/get-course-student-registed")
    public ResponseEntity<?> getCourseStudentRegisted(@RequestParam String semester,@RequestParam String studentEmail){
        return BaseResponse.ok(courseService.getRegistedCourse(studentEmail,semester));
    }

    @PostMapping("/register-for-student")
    public ResponseEntity<?> registerClassForStudent(@RequestBody ClassRegisterRequest classRegisterRequest){
        return null;
        //TODO:
    }
}

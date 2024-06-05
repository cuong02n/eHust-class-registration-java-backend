package vn.edu.hust.ehustclassregistrationjavabackend.controller.student;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.StudentClassRegistrationRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

@RestController
@RequestMapping("/students/classes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT')")
public class ClassController {
    final ClassService classService;
    private final HttpServletRequest request;

    @PostMapping("/register-class")
    public ResponseEntity<?> registerClass(@RequestBody StudentClassRegistrationRequest studentClassRegistrationRequest) {
        return BaseResponse.created(classService.registerClassByStudent(studentClassRegistrationRequest), "Lỗi: ban chưa đăng ký được");
    }

    @GetMapping("/register-class")
    public ResponseEntity<?> getRegistedClass(@RequestParam String semester) {
        User user = (User) request.getAttribute("user");
        return BaseResponse.ok(classService.getStudentRegisted(user.getEmail(), semester));
    }

    @DeleteMapping("/register-class")
    public ResponseEntity<?> unRegisterClass(@RequestBody StudentClassRegistrationRequest studentClassRegistrationRequest) {
        return BaseResponse.created(classService.unRegisterClassByStudent(studentClassRegistrationRequest));
    }

}

package vn.edu.hust.ehustclassregistrationjavabackend.controller.student;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.ChangeClassRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.StudentClassRegisterRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

import java.util.List;

@RestController
@RequestMapping("/api/students/classes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT')")
@EnableAsync
public class ClassController {
    final ClassService classService;
    private final HttpServletRequest request;

    @PostMapping("/register-class")
    public ResponseEntity<?> registerClass(@Valid @RequestBody StudentClassRegisterRequest studentClassRegisterRequest) {
        return BaseResponse.created(classService.registerClassByStudent(studentClassRegisterRequest), "Lỗi: ban chưa đăng ký được");
    }

    @GetMapping("/register-class")
    public ResponseEntity<?> getRegisteredClass(@RequestParam String semester) {
        User user = (User) request.getAttribute("user");
        return BaseResponse.ok(classService.getStudentRegistered(user.getEmail(), semester));
    }



    @PostMapping("/change-class")
    public ResponseEntity<?> changeToSimilarClass(@RequestBody ChangeClassRequest changeClassRequest){
        User user = (User) request.getAttribute("user");
        return BaseResponse.ok(classService.changeToSimilarClass(changeClassRequest));
    }

    @DeleteMapping("/register-class")
    public ResponseEntity<?> unRegisterClass(@Valid @RequestBody StudentClassRegisterRequest studentClassRegisterRequest) {
        return BaseResponse.created(classService.unRegisterClassByStudent(studentClassRegisterRequest));
    }

}

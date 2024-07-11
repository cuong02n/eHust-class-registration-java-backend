package vn.edu.hust.ehustclassregistrationjavabackend.controller.superadmin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;

import java.util.List;

@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
@RestController
@RequestMapping("/api/super-admin/user")
@RequiredArgsConstructor
public class SuperAdminUserController {
    final UserService userService;
    private final CourseService courseService;

    @GetMapping("/get-all-student")
    public ResponseEntity<?> getAllStudent() {
        return BaseResponse.ok(userService.getAllStudent());
    }

    @GetMapping("/get-all-admin")
    public ResponseEntity<?> getAllAdmin() {
        return BaseResponse.ok(userService.getAllAdmin());
    }

    @PostMapping("/update-user")
    public ResponseEntity<?> updateUsers(@RequestBody List<@Valid User> users) {
        return BaseResponse.ok(userService.updateUsers(users));
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody List<String> emails) {
        return BaseResponse.ok(userService.activate(emails));
    }

    @PostMapping("/de-activate")
    public ResponseEntity<?> deActivate(@RequestBody List<String> emails) {
        return BaseResponse.ok(userService.deActivate(emails));
    }

    @PostMapping("/insert-registration")
    public ResponseEntity<?> insertRegistration(@RequestBody List<UserCourseRegistration> registrations) {
        return BaseResponse.ok(courseService.insertUserCourseRegistration(registrations));
    }
}

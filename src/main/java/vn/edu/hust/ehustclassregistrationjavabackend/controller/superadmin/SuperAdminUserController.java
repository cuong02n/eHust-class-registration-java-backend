package vn.edu.hust.ehustclassregistrationjavabackend.controller.superadmin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

import java.util.List;

@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
@RestController
@RequestMapping("/api/super-admin/user")
@RequiredArgsConstructor
public class SuperAdminUserController {
    final UserService userService;
    @GetMapping("/get-all-student")
    public ResponseEntity<?> getAllStudent(){
        return BaseResponse.ok(userService.getAllStudent());
    }
    @GetMapping("/get-all-admin")
    public ResponseEntity<?> getAllAdmin(){
        return BaseResponse.ok(userService.getAllAdmin());
    }

    @PostMapping("/update-students")
    public ResponseEntity<?> updateStudents(@RequestBody List<User> students){
        return BaseResponse.ok(userService.updateStudents(students));
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody List<String> emails){
        return BaseResponse.ok(userService.activate(emails));
    }
}

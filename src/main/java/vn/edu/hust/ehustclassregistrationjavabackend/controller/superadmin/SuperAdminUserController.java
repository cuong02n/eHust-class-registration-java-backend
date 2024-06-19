package vn.edu.hust.ehustclassregistrationjavabackend.controller.superadmin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

import java.util.List;

@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
@RestController
@RequestMapping("/super-admin/user")
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
}

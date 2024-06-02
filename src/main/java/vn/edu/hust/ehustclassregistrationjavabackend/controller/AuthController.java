package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.AuthEmailPasswordRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.AuthService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    final UserService userService;
    private final JwtUtils jwtUtils;
    final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthEmailPasswordRequest request) {
        System.out.println("User login");
        var ret =  BaseResponse.createBaseResponse(authService.login(request),200,401,"Sai tài khoản/Mật khẩu");
        System.out.println(ret);
        return ret;
    }
}

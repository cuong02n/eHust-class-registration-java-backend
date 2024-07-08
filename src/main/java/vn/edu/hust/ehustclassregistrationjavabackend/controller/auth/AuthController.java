package vn.edu.hust.ehustclassregistrationjavabackend.controller.auth;

import jakarta.annotation.security.RunAs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.auth.AuthEmailPasswordRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.auth.AuthResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.AuthService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils;

import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
//@EnableAsync
public class AuthController {
    final UserService userService;
    private final JwtUtils jwtUtils;
    final AuthService authService;
    ExecutorService executors = Executors.newFixedThreadPool(2);
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthEmailPasswordRequest request) {
        return BaseResponse.createBaseResponse(authService.login(request),200,401,"Sai tài khoản/Mật khẩu");
    }
}

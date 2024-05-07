package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.AuthIdPasswordRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.AuthResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthIdPasswordRequest request) {
        String accessToken = jwtUtils.generateAccessToken(userService.loadUserByUsername(request.getId()));
        return BaseResponse.ok(new AuthResponse(accessToken, (jwtUtils.getExpirationDate(accessToken).getTime() - System.currentTimeMillis()) / 1000), "User not exist");
    }
}

package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.AuthIdPasswordRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthIdPasswordRequest request) {
        return null;
    }
}

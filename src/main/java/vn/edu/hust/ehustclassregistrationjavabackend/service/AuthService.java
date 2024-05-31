package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.config.SecurityConfig;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.AuthEmailPasswordRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.AuthResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils;

import static vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils.ACCESS_TOKEN_EXPIRED;

@Service
@RequiredArgsConstructor
public class AuthService {
    final UserRepository userRepository;
    private final UserService userService;
    final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponse login(AuthEmailPasswordRequest request) {
        UserDetails user = userService.loadUserByUsername(request.getEmail());
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            long expired = System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED;
            return new AuthResponse(jwtUtils.generateAccessToken(user, expired), ACCESS_TOKEN_EXPIRED/1000);
        }
        throw new MessageException("Sai mật khẩu");
    }
}

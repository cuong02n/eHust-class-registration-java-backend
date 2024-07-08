package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.auth.AuthEmailPasswordRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.auth.AuthResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils;

import static vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils.ACCESS_TOKEN_EXPIRED;

@Service
@RequiredArgsConstructor
@EnableAsync
public class AuthService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthResponse login(AuthEmailPasswordRequest request) {
        User user = userService.loadUserByUsername(request.getEmail());
        if (user == null) {
            throw new MessageException("Tài khoản không tồn tại", HttpStatus.UNAUTHORIZED);
        }
//        System.out.println("start"+System.currentTimeMillis()%10000);
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            System.out.println("end"+System.currentTimeMillis()%10000);

            long expired = System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED;
            return new AuthResponse(jwtUtils.generateAccessToken(user, expired), expired, user.getUsername(), user.getRole());
        }
        throw new MessageException("Sai mật khẩu", HttpStatus.UNAUTHORIZED);
    }
}

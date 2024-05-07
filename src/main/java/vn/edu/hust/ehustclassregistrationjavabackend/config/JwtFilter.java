package vn.edu.hust.ehustclassregistrationjavabackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils;

import java.io.IOException;

//@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    final JwtUtils jwtUtils;
    final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
//        String jwt = request.getHeader("Authorization");
//        System.out.println("Doing filter internal");
//        if (jwt != null && jwt.startsWith("Bearer ")) {
//            jwt = jwt.substring(7);
//            String id = jwtUtils.extractId(jwt);
//            if (id != null) {
//                request.setAttribute("user", userRepository.findById(id).orElse(null));
//                filterChain.doFilter(request, response);
//            }
//        }
        System.out.println("Auth: "+request.getHeader("Authorization"));
        filterChain.doFilter(request, response);
    }
}

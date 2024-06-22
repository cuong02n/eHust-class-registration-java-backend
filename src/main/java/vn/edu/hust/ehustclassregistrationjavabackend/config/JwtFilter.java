package vn.edu.hust.ehustclassregistrationjavabackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.JwtUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    final JwtUtils jwtUtils;
    final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            System.out.println(authHeader);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authHeader.substring(7);
            String userId = jwtUtils.extractId(token);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userService.loadUserByUsername(userId);
                if(!user.isEnabled()){
//                    filterChain.doFilter(request,response);
                    throw new MessageException("Tài khoản của bạn đã hết hạn", HttpStatus.UNAUTHORIZED);
                }
                request.setAttribute("user", user);

                if (jwtUtils.isTokenValid(token, user)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities()
                    );
                    upaToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(upaToken);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            request.setAttribute("user",null);
            filterChain.doFilter(request,response);
        }
    }
}

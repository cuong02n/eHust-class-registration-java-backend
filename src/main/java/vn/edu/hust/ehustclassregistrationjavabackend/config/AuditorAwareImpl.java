package vn.edu.hust.ehustclassregistrationjavabackend.config;

import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuditorAwareImpl implements AuditorAware<String> {
    final HttpServletRequest request;
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
//        User audit = (User)request.getAttribute("user");
//        if(audit == null) {
//            return Optional.empty();
//        }
//        System.out.println("audit is "+audit);
//        return Optional.of(audit.getEmail());
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated())
            return Optional.empty();
        User audit = (User) authentication.getPrincipal();
        return Optional.of(audit.getEmail());
    }
}

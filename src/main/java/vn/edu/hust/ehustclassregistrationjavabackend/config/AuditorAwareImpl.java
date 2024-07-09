package vn.edu.hust.ehustclassregistrationjavabackend.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {
    final HttpServletRequest request;
    @Override
    public Optional<String> getCurrentAuditor() {
        User audit = (User)request.getAttribute("user");
        if(audit == null) {
            return Optional.empty();
        }
        System.out.println("audit is "+audit);
        return Optional.of(audit.getEmail());
    }
}

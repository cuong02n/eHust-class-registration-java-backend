package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;

@AllArgsConstructor
@Data
public class AuthResponse {
    @Expose
    String token;
    @Expose
    Long expires;
    @Expose
    String email;
    @Expose
    User.Role role;
}

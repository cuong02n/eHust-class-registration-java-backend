package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {
    @Expose
    String token;
    @Expose
    Long expiredAt;
}

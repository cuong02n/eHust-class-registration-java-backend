package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    String token;
    Long expiredIn;
}

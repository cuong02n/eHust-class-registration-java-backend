package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NonNull;

@Data
public class AuthEmailPasswordRequest {
    @SerializedName("email")
    @NonNull
    String email;

    @SerializedName("password")
    @NonNull
    String password;
}

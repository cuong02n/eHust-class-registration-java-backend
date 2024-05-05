package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NonNull;

@Data
public class AuthIdPasswordRequest {
    @SerializedName("id")
    @NonNull
    String id;

    @SerializedName("password")
    @NonNull
    String password;
}

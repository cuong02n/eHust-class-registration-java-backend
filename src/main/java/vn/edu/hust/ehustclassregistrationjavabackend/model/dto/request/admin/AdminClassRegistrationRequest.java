package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AdminClassRegistrationRequest {
    @Expose
    @NonNull
    String studentEmail;

    @Expose
    @NonNull
    String semester;
    @Expose
    @NonNull
    List<String> classIds;
}

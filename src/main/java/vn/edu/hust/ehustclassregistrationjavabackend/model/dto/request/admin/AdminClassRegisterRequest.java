package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AdminClassRegisterRequest {
    @NonNull
    @Expose
    String studentEmail;
    @NonNull
    @Expose
    String semester;
    @NonNull
    @Expose
    List<String> classIds;
}

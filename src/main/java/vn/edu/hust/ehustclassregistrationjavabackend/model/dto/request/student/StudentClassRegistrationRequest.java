package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class StudentClassRegistrationRequest {
    @Expose
    @NonNull
    String semester;
    @Expose
    @NonNull
    @Size(min = 1, max = 20)
    List<String> classIds;
}

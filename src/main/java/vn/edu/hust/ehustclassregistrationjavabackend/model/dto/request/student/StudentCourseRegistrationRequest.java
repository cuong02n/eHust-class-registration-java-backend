package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class StudentCourseRegistrationRequest {
    @Expose
    @NonNull
    String semester;
    @Expose
    @NonNull
    @Size(min = 1, max = 50)
    List<String> courseIds;
}

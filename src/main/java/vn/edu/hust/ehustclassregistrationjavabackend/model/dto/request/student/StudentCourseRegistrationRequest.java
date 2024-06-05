package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student;

import com.google.gson.annotations.Expose;
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
    List<String> courseIds;
}

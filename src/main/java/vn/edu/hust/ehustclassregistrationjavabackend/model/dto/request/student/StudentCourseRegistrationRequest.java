package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.List;

@Data
public class StudentCourseRegistrationRequest {
    @Expose
    String semester;
    @Expose
    List<String> courseIds;
}

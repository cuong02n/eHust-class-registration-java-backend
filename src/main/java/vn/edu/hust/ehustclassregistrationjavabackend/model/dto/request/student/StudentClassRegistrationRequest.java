package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student;

import com.google.gson.annotations.Expose;
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
    List<String> classIds;
}

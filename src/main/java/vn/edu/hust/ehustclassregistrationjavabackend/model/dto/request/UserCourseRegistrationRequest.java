package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UserCourseRegistrationRequest {
    String semester;
    List<String> courseIds;
}

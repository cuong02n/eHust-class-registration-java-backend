package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student;

import lombok.Data;

import java.util.List;

@Data
public class StudentClassRegisterRequest {
    String semester;
    List<String> classIds;
}

package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.common;

import lombok.Data;

import java.util.List;

@Data
public class ClassRegisterRequest {
    String semester;
    List<String> classIds;
}

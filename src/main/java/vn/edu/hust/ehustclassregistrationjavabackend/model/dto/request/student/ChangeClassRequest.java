package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student;

import lombok.Data;
import lombok.NonNull;

@Data
public class ChangeClassRequest {
    @NonNull
    String semester;
    @NonNull
    String oldClassId;
    @NonNull
    String newClassId;
}
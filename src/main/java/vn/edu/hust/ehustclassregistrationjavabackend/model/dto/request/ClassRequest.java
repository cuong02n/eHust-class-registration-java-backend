package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NonNull;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;

import java.util.List;

@Data
public class ClassRequest {
    @NonNull
    String id;
    @Nullable
    String semester;
    @Nullable
    String semesterType;
    @NonNull
    Integer maxStudent;
    @Nullable
    Class.Status status;
    @NonNull
    String courseId;
    @NonNull
    List<Class.Timetable> timetable;
}

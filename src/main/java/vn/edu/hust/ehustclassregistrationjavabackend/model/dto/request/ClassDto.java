package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ClassDto implements Serializable {
    @NonNull
    @Expose
    String id;
    @Nullable
    @Expose
    String semester;
    @Nullable
    @Expose
    String semesterType;
    @NonNull
    @Expose
    Integer maxStudent;
    @Nullable
    @Expose
    Class.Status status;
    @NonNull
    @Expose
    String courseId;
    @NonNull
    @Expose
    List<Class.Timetable> timetable;
}

package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.experimental.NonFinal;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;

@Data
public class CourseRelationshipRequest {
    @Expose
    @Nonnull
    String courseId;
    @Expose
    @Nonnull
    String courseConstraintId;
    @Expose
    @Nonnull
    CourseRelationship.Relation relation;
}

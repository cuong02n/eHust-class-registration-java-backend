package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Getter
@Table(name = "course_relationship")
public class CourseRelationship extends BaseEntity {
    @Id
    @Expose(serialize = false)
    Long id;

    @Column(name = "course_id")
    @Expose
    Long courseId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "course_id", insertable = false, updatable = false)
//    Course course;

    @Column(name = "course_constraint_id")
    @Expose
    Long courseConstraintId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_constraint_id", insertable = false, updatable = false)
    Course courseConstraint;

    @Enumerated(value = EnumType.STRING)
    @Expose
    Relation relation;

    public enum Relation {
        PREREQUISITE,
        PRECOURSE,
        COREQUISITECOURSE
    }
}

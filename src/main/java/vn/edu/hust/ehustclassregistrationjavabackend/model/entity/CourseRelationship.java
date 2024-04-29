package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    Long id;

    @Column(name = "course_id")
    Long courseId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    Course course;

    @Column(name = "course_constraint_id")
    Long courseConstraintId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_constraint_id", insertable = false, updatable = false)
    Course courseConstraint;

    @Enumerated(value = EnumType.STRING)
    Relation relation;
    public enum Relation {
        PREREQUISITE,
        PRECOURSE,
        COREQUISITECOURSE
    }
}

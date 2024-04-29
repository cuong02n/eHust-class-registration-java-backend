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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "course_id", columnDefinition = "bigint not null")
    @Expose
    Long courseId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "course_id", insertable = false, updatable = false)
//    Course course;

    @Column(name = "course_constraint_id", columnDefinition = "bigint not null")
    @Expose
    Long courseConstraintId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_constraint_id", insertable = false, updatable = false)
    Course courseConstraint;

    @Enumerated(value = EnumType.STRING)
    @Expose
    @Column(nullable = false)
    Relation relation;

    public enum Relation {
        PREREQUISITE,
        PRECOURSE,
        COREQUISITECOURSE
    }
}

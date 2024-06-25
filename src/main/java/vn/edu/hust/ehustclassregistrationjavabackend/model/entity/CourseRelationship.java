package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "course_relationship", uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "course_constraint_id"})})
public class CourseRelationship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    Long id;

    @Expose
    @Column(name = "course_id",nullable = false)
    String courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    @Expose(serialize = false)
    Course course;

    @Column(name = "course_constraint_id",nullable = false)
    @Expose
    String courseConstraintId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_constraint_id", insertable = false, updatable = false)
    @Expose(serialize = false)
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

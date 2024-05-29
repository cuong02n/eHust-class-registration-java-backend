package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Table(name = "user_course_registration", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "course_id", "semester"})})
public class UserCourseRegistration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Expose
    @Column(nullable = false)
    String semester;

    @Column(name = "user_email", nullable = false)
    String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @Expose(serialize = false)
    User user;

    @Column(name = "course_id", nullable = false)
    @Expose
    String courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    @Expose(serialize = false)
    Course course;

}

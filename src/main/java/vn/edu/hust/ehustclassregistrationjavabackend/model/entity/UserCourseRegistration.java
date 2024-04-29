package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "user_course_registration")
public class UserCourseRegistration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id")
    String userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @Expose(serialize = false)
    User user;

    @Column(name = "course_id")
    Long courseId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    Course course;
}

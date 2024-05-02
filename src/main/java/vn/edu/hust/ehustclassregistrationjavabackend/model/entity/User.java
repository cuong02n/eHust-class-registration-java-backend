package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "user")
@Builder
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @Expose
    String id;

    @Enumerated(EnumType.STRING)
    @Expose
    @Column(nullable = false)
    Role role;

    @Enumerated(EnumType.STRING)
    @Expose
    StudentType studentType;

    @Expose
    String name;

    @Expose
    String email;

    @Expose
    @Column(columnDefinition = "BIT(1) default 0")
    boolean active;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_course_registration",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Expose
    Set<Course> courseRegisted;

    public enum Role {
        STUDENT
    }

    public enum StudentType {
        ELITECH,
        STANDARD
    }

}

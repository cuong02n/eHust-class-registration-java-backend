package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.*;

import java.lang.annotation.Native;
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

    @Expose
    String name;

    @Expose
    String email;

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

}

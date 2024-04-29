package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @Expose
    String id;

    @Enumerated(EnumType.STRING)
    @Expose
    Role role;

    @Expose
    String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

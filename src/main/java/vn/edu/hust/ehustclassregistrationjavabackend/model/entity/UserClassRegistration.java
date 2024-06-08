package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Table(name = "user_class_registration")
@Builder
@AllArgsConstructor
public class UserClassRegistration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    @Expose
    private String email;

    @JoinColumn(name = "email", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose(serialize = false)
    User user;

    @Column(name = "class_id")
    @Expose
    String classId;

    @Column(name = "semester")
    @Expose
    String semester;

    @JoinColumn(name = "class_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JoinColumn(name = "semester", referencedColumnName = "semester", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @SerializedName("class")
    @Expose
    private Class aClass;

}

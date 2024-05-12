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

    @Column(name = "user_id", nullable = false)
    @Expose
    private String userId;

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose
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
    private Class aClass;

}

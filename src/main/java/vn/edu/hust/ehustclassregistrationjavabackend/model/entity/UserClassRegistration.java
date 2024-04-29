package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Table(name = "user_class_registration")
public class UserClassRegistration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private String userId;

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Column(name = "class_id",nullable = false)
    private Long classId;

    @JoinColumn(name = "class_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @SerializedName("class")
    private Class aClass;

}

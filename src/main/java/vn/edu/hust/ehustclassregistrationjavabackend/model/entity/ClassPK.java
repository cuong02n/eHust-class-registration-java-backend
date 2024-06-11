package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class ClassPK implements Serializable {
    @Column(name = "id")
    @Expose
    @NonNull
    String id;
    @Column(name = "semester")
    @Expose
    String semester;
}

package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class MetadataPk {
    @Column(name = "name")
    @Expose
    String name;

    @Column(name = "semester")
    @Nullable
    @Expose
    String semester = "";
}

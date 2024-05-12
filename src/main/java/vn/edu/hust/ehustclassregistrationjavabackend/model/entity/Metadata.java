package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Data
public class Metadata extends BaseEntity {
    @EmbeddedId
    @Expose
    MetadataPk metadataPk;
    @Expose
    @Column(nullable = false)
    String value;

    @Override
    public String toString() {
        return GsonUtil.gsonExpose.toJson(this);
    }
}

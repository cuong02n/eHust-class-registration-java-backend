package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import com.google.gson.annotations.Expose;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
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

    public enum MetadataKey {
        START_WEEK_1,
        CURRENT_SEMESTER,
        START_REGISTER_CLASS_OFFICIAL_STANDARD,
        START_REGISTER_CLASS_UNOFFICIAL_STANDARD,
        START_REGISTER_CLASS_OFFICIAL_ELITECH,
        START_REGISTER_CLASS_UNOFFICIAL_ELITECH,
        START_REGISTER_FREE,

        END_REGISTER_CLASS_OFFICIAL_STANDARD,
        END_REGISTER_CLASS_UNOFFICIAL_STANDARD,
        END_REGISTER_CLASS_OFFICIAL_ELITECH,
        END_REGISTER_CLASS_UNOFFICIAL_ELITECH,
        END_REGISTER_FREE,

        START_REGISTER_COURSE,
        END_REGISTER_COURSE,
    }
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @Data
    public static class MetadataPk{
        @Expose
        @Enumerated(EnumType.STRING)
        @Nonnull
        MetadataKey metadataKey;

        @Expose
        String semester;
    }
}

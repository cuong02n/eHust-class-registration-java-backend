package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.superadmin;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;


@Data
public class SuperAdminMetadataRequest {
    @Nonnull
    Metadata.MetadataKey key;
    @Size(min = 4,max = 5)
    String semester;
    String value;
}

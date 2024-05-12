package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin;

import lombok.Data;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.MetadataPk;

@Data
public class MetadataRequest {
    String name;
    String semester;
    String value;

    public Metadata toEntity(){
        return new Metadata(new MetadataPk(name,semester),value);
    }
}

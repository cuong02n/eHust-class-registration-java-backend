package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Nonnull;
import lombok.Data;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;

import java.util.List;

@Data
public class ClassCreateRequest {
    @Expose
    String semester;
    @Expose
    @Nonnull
    List<ClassDto> classes;
}

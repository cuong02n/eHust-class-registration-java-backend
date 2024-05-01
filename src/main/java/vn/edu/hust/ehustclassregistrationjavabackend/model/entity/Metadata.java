package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Data
public class Metadata extends BaseEntity {
    @Id
    @Expose
    String name;
    @Expose
    @Column(nullable = false)
    String value;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

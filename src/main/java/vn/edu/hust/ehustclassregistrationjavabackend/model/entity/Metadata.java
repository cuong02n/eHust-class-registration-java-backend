package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Metadata extends BaseEntity{
    @Id
    String name;
    @Column(nullable = false)
    String value;
}

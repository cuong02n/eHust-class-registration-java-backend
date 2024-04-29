package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Metadata extends BaseEntity{
    @Id
    String name;
    String value;

}

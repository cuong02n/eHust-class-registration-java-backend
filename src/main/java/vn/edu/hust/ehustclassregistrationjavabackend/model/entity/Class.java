package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Class {
    @Id
    Long id;
    Long classCode;
    Long relatedClassCode;
    String semesterType;
    String semester; // 20231
    int maxStudent;
    boolean isOpen;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Course course;

}

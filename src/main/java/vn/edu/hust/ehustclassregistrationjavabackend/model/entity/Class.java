package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"class_code", "status", "semester"})})
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Expose
    @Column(nullable = false, columnDefinition = "bigint not null")
    Long classCode;
    @Expose
    Long relatedClassCode;
    @Expose
    String semesterType;
    @Expose
    @Column(nullable = false)
    String semester; // 20231

    @Expose
    @Column(nullable = false)
    int maxStudent;

    @Expose
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Status status = Status.NOT_YET_OPEN;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Expose
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    public enum Status {
        NOT_YET_OPEN,
        OPEN,
        CLOSE,
        CANCEL
    }
}

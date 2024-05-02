package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Class {
    @EmbeddedId
    @Expose
    ClassPK classPK;

    @Expose
    String semesterType;

    @Expose
    @Column(nullable = false)
    int maxStudent;

    @Expose
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Status status = Status.NOT_YET_OPEN;

    @Column(name = "course_id", nullable = false)
    @Expose
    String courseId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Expose
    @JoinColumn(name = "course_id", nullable = false, insertable = false, updatable = false)
    Course course;

    public enum Status {
        NOT_YET_OPEN,
        OPEN,
        CLOSE,
        CANCEL
    }
}

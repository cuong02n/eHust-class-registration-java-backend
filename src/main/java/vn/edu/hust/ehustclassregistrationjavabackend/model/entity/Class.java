package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Class extends BaseEntity {
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
    Status status = Status.OPEN;

    @Column(name = "course_id", nullable = false)
    @Expose
    String courseId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Expose(serialize = false)
    @JoinColumn(name = "course_id", nullable = false, insertable = false, updatable = false)
    Course course;

    @Expose
    @Column(columnDefinition = "json not null")
    String timetable;

    public enum Status {
        OPEN,
        CLOSE,
        CANCEL
    }

    @Data
    public static class Timetable implements Serializable{
        @NonNull
        @SerializedName("week")
        String week;

        @NonNull
        @SerializedName("from")
        Integer from;

        @NonNull
        @SerializedName("to")
        Integer to;

        @NonNull
        @SerializedName("place")
        String place;

        @NonNull
        @SerializedName("dayOfWeek")
        String dayOfWeek; // 2-8
    }

}

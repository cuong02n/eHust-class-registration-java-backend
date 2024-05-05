package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Class extends BaseEntity {
    static TypeToken<List<Timetable>> timetableListTypetoken = new TypeToken<>() {
    };
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
    @Builder.Default
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

    public ClassDto toClassDto() {
        return ClassDto.builder()
                .id(classPK.id)
                .semester(classPK.semester)
                .semesterType(semesterType)
                .maxStudent(maxStudent)
                .status(status)
                .courseId(courseId)
                .timetable(GsonUtil.gsonExpose.fromJson(timetable, timetableListTypetoken))
                .build();
    }

    public enum Status {
        OPEN,
        CLOSE,
        CANCEL
    }

    @Data
    public static class Timetable implements Serializable {
        @NonNull
        @SerializedName("week")
        @Expose
        String week;

        @NonNull
        @SerializedName("from")
        @Expose
        Integer from;

        @NonNull
        @Expose
        @SerializedName("to")
        Integer to;

        @NonNull
        @Expose
        @SerializedName("place")
        String place;

        @NonNull
        @Expose
        @SerializedName("dayOfWeek")
        String dayOfWeek; // 2-8
    }
}

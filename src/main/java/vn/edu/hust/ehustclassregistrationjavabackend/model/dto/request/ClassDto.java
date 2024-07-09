package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.TimetableUtil;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ClassDto implements Serializable {
    @NonNull
    @Expose
    String id;
    @NonNull
    @Expose
    String semester;
    @NonNull
    @Expose
    String semesterType;
    @NonNull
    @Expose
    Integer maxStudent;

    @Expose
    @Nullable
    String theoryClassId;

    @Expose
    @NonNull
    Class.ClassType classType;

    @Expose
    @NonNull
    Integer credit;
    @NonNull
    @Expose
    Class.Status status;
    @NonNull
    @Expose
    String courseId;
    @Nullable
    @Expose
    @Singular
    List<Class.Timetable> timetables;

    @Expose
    @Nullable
    String teacherEmail;

    @Expose
    @NonNull
    boolean needExperiment;

    public Class toClassEntity(User modified) {
        Class entity = Class.builder()
                .classPK(new ClassPK(id, semester))
                .semesterType(semesterType)
                .maxStudent(maxStudent)
                .status(status)
                .courseId(courseId)
                .timetable(TimetableUtil.toString(timetables))
                .theoryClassId(theoryClassId)
                .classType(classType)
                .teacherEmail(teacherEmail)
                .build();
        entity.setUserModified(modified);
        return entity;
    }
}

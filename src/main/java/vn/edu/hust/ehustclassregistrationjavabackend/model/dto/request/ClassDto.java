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
    @Nullable
    @Expose
    String semester;
    @Nullable
    @Expose
    String semesterType;
    @NonNull
    @Expose
    Integer maxStudent;

    @Expose
    @Nullable
    Integer currentRegisted;

    @Expose
    String theoryClassId;

    @Expose
    Class.ClassType classType;

    @Nullable
    @Expose
    Class.Status status;
    @NonNull
    @Expose
    String courseId;
    @NonNull
    @Expose
    @Singular
    List<Class.Timetable> timetables;

    @Expose
    String teacherEmail;

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

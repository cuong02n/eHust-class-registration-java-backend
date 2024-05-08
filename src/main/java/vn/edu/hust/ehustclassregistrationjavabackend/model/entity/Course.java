package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Course extends BaseEntity {
    @Id
    @SerializedName("id")
    @Expose
    String id;
    @Expose
    String courseName;
    @Expose
    String courseNameE;
    @Expose
    @Column(columnDefinition = "text")
    String description;
    @Expose
    @Column(columnDefinition = "int not null")
    Integer credit; // 3
    @Expose
    String creditInfo; // (3-0-3-6)
    @Expose
    @Enumerated(EnumType.STRING)
    CourseType courseType;
    //    @Expose
//    Integer theoryHour;
//    @Expose
//    Integer assignmentHour;
//    @Expose
//    Integer practiceHour;
//    @Expose
//    Integer selfStudyHour;
//    @Expose
//    Integer internHour;
//    @Expose
//    String departmentName;
    @Expose
    String schoolName;

    //    @Expose
//    @Column(columnDefinition = "int not null default 0")
//    int version;
    @Expose
    @Column(columnDefinition = "bit not null default 0")
    Boolean needExperiment;

    @OneToMany(mappedBy = "courseId", fetch = FetchType.EAGER)
    @SQLRestriction("relation = 'PREREQUISITE'")
    @Expose
    List<CourseRelationship> preRequisiteCourses;

    @OneToMany(mappedBy = "courseId", fetch = FetchType.EAGER)
    @SQLRestriction("relation = 'PRECOURSE'")
    @Expose
    List<CourseRelationship> preCourse;

    @OneToMany(mappedBy = "courseId", fetch = FetchType.EAGER)
    @SQLRestriction("relation = 'COREQUISITECOURSE'")
    @Expose
    List<CourseRelationship> coreQuisiteCourse;

    @Override
    public String toString() {
        return GsonUtil.gsonExpose.toJson(this);
    }

    public enum CourseType {
        STANDARD,
        ELITECH
    }
}

package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Where;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course")
@Getter
public class Course extends BaseEntity {
    @Id
    @SerializedName("id")
    @Expose
    Long id;
    @Expose
    String courseCode;
    @Expose
    String courseName;
    @Expose
    String courseNameE;
    @Expose
    String description;
    @Expose
    Integer credit;
    @Expose
    String creditInfo;
    @Expose
    String gdCreditInfo;
    @Expose
    Integer courseType;
    @Expose
    Integer theoryHour;
    @Expose
    Integer assignmentHour;
    @Expose
    Integer practiceHour;
    @Expose
    Integer selfStudyHour;
    @Expose
    Integer internHour;
    @Expose
    String departmentName;
    @Expose
    String schoolName;
    @Expose
    boolean needExperiment;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Where(clause = "relation = 'PREREQUISITE'")
    @Expose
    List<CourseRelationship> preRequisiteCourses;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Where(clause = "relation = 'PRECOURSE'")
    @Expose
    List<CourseRelationship> preCourse;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Where(clause = "relation = 'COREQUISITECOURSE'")
    @Expose
    List<CourseRelationship> coreQuisiteCourse;

}

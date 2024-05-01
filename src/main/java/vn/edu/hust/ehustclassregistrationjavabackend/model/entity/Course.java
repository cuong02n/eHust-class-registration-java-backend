package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course", uniqueConstraints = {@UniqueConstraint(columnNames = {"course_code", "active"})})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course extends BaseEntity {
    @Id
    @SerializedName("id")
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Expose
    @Column(columnDefinition = "varchar(255) not null")
    String courseCode;
    @Expose
    String courseName;
    @Expose
    String courseNameE;
    @Expose
    @Column(columnDefinition = "text")
    String description;
    @Expose
    @Column(columnDefinition = "int not null")
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
    @Column(columnDefinition = "bit not null default 0")
    boolean needExperiment;
    @Expose
    @Column(columnDefinition = "bit not null default 1")
    boolean active;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @SQLRestriction("relation = 'PREREQUISITE'")
    @Expose
    List<CourseRelationship> preRequisiteCourses;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @SQLRestriction("relation = 'PRECOURSE'")
    @Expose
    List<CourseRelationship> preCourse;

    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @SQLRestriction("relation = 'COREQUISITECOURSE'")
    @Expose
    List<CourseRelationship> coreQuisiteCourse;

}

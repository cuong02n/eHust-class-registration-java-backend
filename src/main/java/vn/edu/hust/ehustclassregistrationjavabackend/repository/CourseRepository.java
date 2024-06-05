package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findAllById(String courseId);

    List<Course> findAllByIdIn(List<String> courseIds);

    @Query("select sum(c.credit) from Course c where c.id in :courseIds")
    int sumCreditByCourseIds(List<String> courseIds);

    @Query("select c.id from Course c, Class cl where c.id = cl.courseId and cl.classPK.id in :classIds and cl.classPK.semester= :semester")
    List<String> getCourseIdsByClassIdsAndSemester(List<String> classIds,String semester);
}

package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findAllByActiveTrue();
    Course findByActiveTrueAndCourseCode(String courseCode);
}

package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,String> {
    List<Course> findAllById(String courseId);
}

package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;

public interface CourseRepository extends JpaRepository<Course,Long> {
}

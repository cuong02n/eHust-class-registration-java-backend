package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;

import java.util.List;

public interface CourseRelationshipRepository extends JpaRepository<CourseRelationship,Long> {
    @Transactional
    List<CourseRelationship> deleteAllByCourseConstraintIdOrCourseId(String courseConstraintId,String courseId);
}

package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;

public interface CourseRelationshipRepository extends JpaRepository<CourseRelationship,Long> {
}

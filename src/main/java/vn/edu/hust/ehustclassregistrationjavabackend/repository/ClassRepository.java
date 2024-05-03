package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class,Long> {
    List<Class> findAllByCourseId(String courseId);
}

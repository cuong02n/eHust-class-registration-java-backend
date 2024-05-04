package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class,Long> {
    List<Class> findAllByCourseId(String courseId);

    boolean existsByClassPK(ClassPK classPK);
    Class findByClassPK(ClassPK classPK);
}

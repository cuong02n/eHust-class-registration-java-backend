package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, ClassPK> {

    List<Class> findAllByCourseIdAndClassPK_Semester(String courseId, String semester);

    List<Class> findAllByClassPK_Semester(String semester);

    List<Class> findAllByClassPKIn(List<ClassPK> classPKS);

    boolean existsByClassPKIn(List<ClassPK> classPKS);
    Optional<Class> findByClassPK(ClassPK classPK);

    List<Class> findAllByClassPK_SemesterAndClassPK_IdIn(String semester,List<String> classIds);

}

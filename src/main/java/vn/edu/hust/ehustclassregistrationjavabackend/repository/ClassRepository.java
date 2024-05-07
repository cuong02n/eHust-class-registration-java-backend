package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class,Long> {
    List<Class> findAllByCourseIdAndClassPK_Semester(String courseId,String semester);

    boolean existsByClassPK(ClassPK classPK);
    Class findByClassPK(ClassPK classPK);
}

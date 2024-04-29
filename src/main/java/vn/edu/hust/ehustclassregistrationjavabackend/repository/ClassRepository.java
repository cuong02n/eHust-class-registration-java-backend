package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
public interface ClassRepository extends JpaRepository<Class,Long> {
    @Query("SELECT count(*) from Class c,UserClassRegistration u where c.id = :classId and u.classId = :classId and c.semester = :semester")
    long countRegistedByClassId(Long classId, String semester);
}

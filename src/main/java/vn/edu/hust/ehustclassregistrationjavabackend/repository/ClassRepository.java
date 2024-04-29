package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class,Long> {
    @Query("SELECT count(*) from Class c,UserClassRegistration u where c.id = :classId and u.classId = :classId and c.semester = :semester")
    long countRegistedByClassId(Long classId, String semester);


    Optional<Class> findClassByClassCode(Long classCode);
    @Query("select class from Class class, Course course where class.status <> 'CANCEL' and class.courseId = course.id and class.semester = :semester and course.courseCode = :courseCode")
    Optional<Class> findClassByCourseCodeAndSemester(String courseCode,String semester);
}

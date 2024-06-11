package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourseRegistration,String> {
    long deleteAllByEmailAndSemesterAndCourseIdIn(String email,String semester,List<String> courseIds);

    List<UserCourseRegistration> findAllByEmailAndSemester(String email,String semester);

    Optional<UserCourseRegistration> findByCourseIdAndSemesterAndEmail(String courseId,String semester,String userId);

    @Query("select sum(c.credit) from UserCourseRegistration r,Course c where r.email =:email and r.semester = :semester and r.courseId = c.id")
    int sumCreditRegistedByEmailAndSemester(String email,String semester);
}

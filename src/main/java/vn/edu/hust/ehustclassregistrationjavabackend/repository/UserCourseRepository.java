package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourseRegistration,Long> {
    long deleteAllByUserIdAndSemesterAndCourseIdIn(String userId,String semester,List<String> courseIds);
    List<UserCourseRegistration> findAllByUserIdAndSemester(String userId,String semester);

    Optional<UserCourseRegistration> findByCourseIdAndSemesterAndUserId(String courseId,String semester,String userId);

    @Query("select sum(c.credit) from UserCourseRegistration r,Course c where r.userId =:userId and r.semester = :semester and r.courseId = c.id")
    int sumCreditRegistedByUserIdAndSemester(String userId,String semester);
}

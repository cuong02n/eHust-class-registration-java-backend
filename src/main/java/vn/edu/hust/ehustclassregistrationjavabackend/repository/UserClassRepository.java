package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserClassRegistration;

import java.util.Optional;

public interface UserClassRepository extends JpaRepository<UserClassRegistration, Long> {
    Optional<UserClassRegistration> findByUserIdAndClassIdAndSemester(String userId, String classId, String semester);

    @Query("select sum (co.credit) from UserClassRegistration reg ,Class cl,Course co where reg.userId = :userId and reg.semester = :semester and reg.classId = cl.classPK.id and cl.courseId = co.id")
    int sumCreditByUserIdAndSemester(String userId, String semester);

    @Query("select count(*) from UserClassRegistration reg where reg.classId = :classId and reg.semester = :semester")
    int countRegistedByClassIdAndSemester(String classId,String semester);
}

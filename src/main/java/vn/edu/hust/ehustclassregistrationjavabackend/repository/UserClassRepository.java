package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserClassRegistration;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "user_register_class")
public interface UserClassRepository extends JpaRepository<UserClassRegistration, Long> {
    Logger log = LoggerFactory.getLogger(UserClassRepository.class);

    Optional<UserClassRegistration> findByEmailAndClassIdAndSemester(String email, String classId, String semester);

    @Query("select sum (co.credit) from UserClassRegistration reg ,Class cl,Course co where reg.email = :email and reg.semester = :semester and reg.classId = cl.classPK.id and cl.courseId = co.id")
    int sumCreditByEmailAndSemester(String email, String semester);

    @Cacheable(key = "#classId + #semester")
    @Query("select count(*) from UserClassRegistration req where req.classId = :classId and req.semester = :semester")
    int countRegisteredClass(String classId, String semester);

    @CacheEvict(key = "#classId + #semester")
    default void evictCache(String classId, String semester) {
        log.info("evict registered key {} {}", classId,semester);
    }

    @Query("select u from UserClassRegistration u where u.email = :email and u.semester = :semester")
    List<UserClassRegistration> getStudentRegistered(String email, String semester);

    /**
     * Should evict cache at service layer, this method does not contain cache handle
     */
    @Transactional
    List<UserClassRegistration> deleteAllBySemesterAndClassIdIn(String semester, List<String> classIds);

}

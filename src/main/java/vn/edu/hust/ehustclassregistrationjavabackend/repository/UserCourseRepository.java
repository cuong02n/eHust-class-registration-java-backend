package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourseRegistration,Long> {
    long deleteAllByUserIdAndSemesterAndCourseIdIn(String userId,String semester,List<String> courseIds);
    List<UserCourseRegistration> findAllByUserIdAndSemester(String userId,String semester);
}

package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;

public interface UserCourseRepository extends JpaRepository<UserCourseRegistration,Long> {
}

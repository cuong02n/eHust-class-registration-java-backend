package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailContaining(String id);

    List<User> findAllByRole(User.Role role);

    List<User> findAllByEmailIn(List<String> emails);

    @Query("update User u set u.active = true where u.active = false and u.email in :emails")
    @Transactional
    long activateUserByEmailIn(List<String> emails);

    @Query("update User u set u.active = false where u.active = true and u.email in :emails")
    @Transactional
    long deActivateUserByEmailIn(List<String> emails);
}

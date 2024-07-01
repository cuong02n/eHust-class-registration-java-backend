package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void createUser(List<User> users) {
        userRepository.saveAll(users);
    }

    public List<User> getAllStudent() {
        return userRepository.findAllByRole(User.Role.ROLE_STUDENT);
    }

    public List<User> getAllAdmin() {
        return userRepository.findAllByRole(User.Role.ROLE_ADMIN);
    }

    public List<User> getAllSuperAdmin() {
        return userRepository.findAllByRole(User.Role.ROLE_SUPER_ADMIN);
    }

    @Override
    public User loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findById(id).orElse(null);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public User findStudentById(String id) {
        return userRepository.findByEmailContaining(id.substring(2)).orElseThrow();
    }

    public List<User> updateStudents(List<User> students) {
        return null;
    }

    public List<String> activate(List<String> emails) {
        List<User> users = userRepository.findAllByEmailIn(emails);
        if (users.size() != emails.size()){
            /**
             * Lỗi không tìm thấy 1 hoặc nhiều
             */
            Set<String> userEmails = users.stream().map(User::getEmail).collect(Collectors.toSet());
            List<String> emailsNotFound = emails.stream().filter(email->!userEmails.contains(email)).toList();
            throw new MessageException("Email sau đây không tồn tại: "+emailsNotFound);
        }
        users.

    }

    public void deActivate(List<String> emails) {

    }
}

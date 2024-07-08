package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = {"users"})
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    final UserRepository userRepository;
    private final HttpServletRequest httpServletRequest;


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
    @Cacheable
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

    @CacheEvict(allEntries = true)
    public String activate(List<String> emails) {
        List<User> users = userRepository.findAllByEmailIn(emails);
        Set<String> userEmails = users.stream().map(User::getEmail).collect(Collectors.toSet());

        if (users.size() != emails.size()){
            /**
             * Lỗi không tìm thấy 1 hoặc nhiều
             */
            List<String> emailsNotFound = emails.stream().filter(email->!userEmails.contains(email)).toList();
            throw new MessageException("Email sau đây không tồn tại: "+emailsNotFound);
        }
        long activatedCount = userRepository.activateUserByEmailIn(emails);
        return "Activated: "+activatedCount;
    }

    /**
     *
     * @param emails: List String
     * @return message
     */
    @CacheEvict(allEntries = true)
    public String deActivate(List<String> emails) {
        List<User> users = userRepository.findAllByEmailIn(emails);
        Set<String> userEmails = users.stream().map(User::getEmail).collect(Collectors.toSet());
        if (users.size() != emails.size()){
            /**
             * Lỗi không tìm thấy 1 hoặc nhiều
             */
            List<String> emailsNotFound = emails.stream().filter(email->!userEmails.contains(email)).toList();
            throw new MessageException("Email sau đây không tồn tại: "+emailsNotFound);
        }
        long deActivateCount = userRepository.deActivateUserByEmailIn(emails);
        return "Deactivated: "+deActivateCount;
    }
}

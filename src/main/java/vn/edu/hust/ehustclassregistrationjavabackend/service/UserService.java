package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    final UserRepository userRepository;

    public String test(int x) {
        if (x == 1) return 0 / 0 + "";
        return x + "";
    }

    public String test2(ClassPK pk) {
        return "";
    }

    public boolean checkUserExist(String userId) {
        return userRepository.existsById(userId);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void createUser(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findById(id).orElse(null);
    }
}

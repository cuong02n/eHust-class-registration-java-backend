package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;

    public boolean checkUserExist(String userId) {
        return userRepository.existsById(userId);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void createUser(List<User> users) {
        userRepository.saveAll(users);
    }
}

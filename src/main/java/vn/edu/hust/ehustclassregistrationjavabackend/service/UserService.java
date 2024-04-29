package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
}

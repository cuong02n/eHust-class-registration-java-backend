package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    final UserRepository userRepository;
}

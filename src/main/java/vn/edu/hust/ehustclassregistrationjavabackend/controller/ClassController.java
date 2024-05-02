package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;

@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {
    final ClassRepository classRepository;
}

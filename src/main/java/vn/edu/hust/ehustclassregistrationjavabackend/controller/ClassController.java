package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {
    final ClassRepository classRepository;

    @GetMapping("/get-by-course-id")
    public ResponseEntity<List<Class>> getClassByCourseId(String courseId) {
        return ResponseEntity.ok().body(classRepository.findAllByCourseId(courseId));
    }

    @PutMapping()
    public ResponseEntity<?> updateClass(@RequestBody Class newClass) {
        try {
            classRepository.save(newClass);
            return ResponseEntity.created(URI.create("/api/class?classId=")).body(newClass);
        }catch (Exception e){
            return ResponseEntity.status(409).body(e.getCause().getMessage());
        }
    }
}

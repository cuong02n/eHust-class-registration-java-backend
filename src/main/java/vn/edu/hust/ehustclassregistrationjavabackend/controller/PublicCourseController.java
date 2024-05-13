package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/public/course")
@RequiredArgsConstructor
public class PublicCourseController {
    final CourseService courseService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllActiveCourses() {
        return BaseResponse.ok(courseService.getAllActiveCourse());
    }

    @GetMapping()
    public ResponseEntity<?> getActiveCourse(@RequestParam List<String> courseIds) {
        return BaseResponse.ok(courseService.getActiveCourse(courseIds));
    }
}

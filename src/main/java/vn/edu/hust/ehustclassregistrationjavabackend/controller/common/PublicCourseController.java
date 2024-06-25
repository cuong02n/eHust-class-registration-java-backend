package vn.edu.hust.ehustclassregistrationjavabackend.controller.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class PublicCourseController {
    final CourseService courseService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCourse() {
        return BaseResponse.ok(courseService.getAllActiveCourse());
    }

    @GetMapping()
    public ResponseEntity<?> getActiveCourse(@RequestParam List<String> courseIds) {
        return BaseResponse.ok(courseService.getActiveCourse(courseIds));
    }
    @GetMapping("/course-relationship")
    public ResponseEntity<?> getRelationship(@RequestParam long relationshipId) {
        return BaseResponse.ok(courseService.getRelationshipById(relationshipId), "Not found relationship");
    }

    @GetMapping("/get-all-relationship")
    public ResponseEntity<?> getAllRelationship() {
        return BaseResponse.ok(courseService.getAllCourseRelationship());
    }
}

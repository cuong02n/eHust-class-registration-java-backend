package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;

import java.net.URI;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final MetadataService metadataService;


    @GetMapping("/")
    public ResponseEntity<Course> getCourse(@RequestParam String courseId) {
        var course = courseService.getActiveCourse(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.ok().body(course.get());
    }

//    @PutMapping("/")

    @PostMapping("/course-relationship")
    public ResponseEntity<?> createCourseRelationship(@RequestBody CourseRelationship relationship) {
        try {
            CourseRelationship created = courseService.insertCourseRelationship(relationship);
            return ResponseEntity.created(new URI("/api/course/get-relationship?relationshipId=" + created.getId())).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getLocalizedMessage());
        }
    }

    @PutMapping("/update-course-relationship")
    public ResponseEntity<?> updateCourseRelationship(@RequestBody CourseRelationship relationship) {
        return null;
        // Todo
    }

    @GetMapping("/course-relationship")
    public ResponseEntity<?> getRelationship(@RequestParam long relationshipId) {
        return BaseResponse.ok(courseService.getRelationshipById(relationshipId), "Not found relationship");
    }
}

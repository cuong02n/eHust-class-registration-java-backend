package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.UserCourseRegistrationRequest;
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

    @GetMapping("")
    public ResponseEntity<?> getCourse(@RequestParam String courseId) {
        return BaseResponse.ok(courseService.getActiveCourse(courseId), "Not found course: ", courseId);
    }

    @PostMapping("")
    public ResponseEntity<?> createCourse(@RequestBody Course newCourse) {
        return BaseResponse.created(courseService.insertCourse(newCourse), "Cannot create course, duplicate id: ", newCourse.getId());
    }

    @PatchMapping("")
    public ResponseEntity<?> updateCourse(@RequestBody Course newCourse){
        return BaseResponse.ok(courseService.updateCourse(newCourse));
    }
    @DeleteMapping("")
    @Operation(description = "Pending",security = @SecurityRequirement(name = "Bearer"))
    public ResponseEntity<?> deleteCourse(@RequestParam String courseId){
        // PENDING
        return BaseResponse.deleted(courseService.deleteCourse(courseId));
    }

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

    @GetMapping("/register-course")
    public ResponseEntity<?> getRegistedCourse(@RequestParam String semester) {
        return BaseResponse.ok(courseService.getRegistedCourse(semester));
    }

    @PostMapping("/register-course")
    public ResponseEntity<?> registerCourse(@RequestBody UserCourseRegistrationRequest request) {
        return BaseResponse.created(courseService.registerCourse(request.getSemester(),request.getCourseIds()));
    }

    @DeleteMapping("/register-course")
    public ResponseEntity<?> unregisterCourse(@RequestBody UserCourseRegistrationRequest request) {
        return BaseResponse.deleted(courseService.unregisterCourse(request.getSemester(),request.getCourseIds()));
    }
}

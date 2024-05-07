package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

import java.net.URI;

@RestController
@RequestMapping("/admin/class")
@RequiredArgsConstructor
public class AdminClassController {
    final CourseService courseService;

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
        return BaseResponse.created(courseService.insertCourseRelationship(relationship));
    }

    @PatchMapping("/update-course-relationship")
    public ResponseEntity<?> updateCourseRelationship(@RequestBody CourseRelationship relationship) {
        return null;
        // Todo
    }
}

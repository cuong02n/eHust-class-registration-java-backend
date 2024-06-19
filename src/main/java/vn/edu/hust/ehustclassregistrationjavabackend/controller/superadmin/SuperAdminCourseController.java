package vn.edu.hust.ehustclassregistrationjavabackend.controller.superadmin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.CourseRelationshipRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/super-admin/courses")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")

public class SuperAdminCourseController {
    final CourseService courseService;

    @PostMapping("")
    public ResponseEntity<?> createCourse(@RequestBody List<Course> newCourse) {
        return BaseResponse.created(courseService.insertCourse(newCourse));
    }

    @PostMapping("/post-course-by-file")
    public ResponseEntity<?> batchClassByExcel(@RequestBody MultipartFile file){
        return BaseResponse.ok(courseService.insertCourses(file));
    }

    @PatchMapping("")
    public ResponseEntity<?> updateCourse(@RequestBody List<Course> newCourse){
        return BaseResponse.ok(courseService.updateCourse(newCourse));
    }
    @DeleteMapping("")
    @Operation(description = "Pending",security = @SecurityRequirement(name = "Bearer"))
    public ResponseEntity<?> deleteCourse(@RequestParam List<String> courseId){
        // PENDING
        return BaseResponse.deleted(courseService.deleteCourse(courseId));
    }

    @PostMapping("/course-relationship")
    public ResponseEntity<?> createCourseRelationship(@RequestBody CourseRelationshipRequest relationship) {
        return BaseResponse.created(courseService.insertCourseRelationship(relationship));
    }

    @PatchMapping("/update-course-relationship")
    public ResponseEntity<?> updateCourseRelationship(@RequestBody CourseRelationship relationship) {
        return null;
        // Todo
    }
}

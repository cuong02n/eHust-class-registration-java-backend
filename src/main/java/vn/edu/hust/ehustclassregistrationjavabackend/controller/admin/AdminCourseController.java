package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.CourseRelationshipRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

@RestController
@RequestMapping("/admin/course")
@RequiredArgsConstructor
public class AdminCourseController {
    final CourseService courseService;

    @PostMapping("")
    public ResponseEntity<?> createCourse(@RequestBody Course newCourse) {
        return BaseResponse.created(courseService.insertCourse(newCourse), "Cannot create course, duplicate id: ", newCourse.getId());
    }

    @PostMapping("/post-course-by-file")
    public ResponseEntity<?> batchClassByExcel(@RequestBody MultipartFile file){
        return BaseResponse.ok(courseService.insertCourses(file));
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
    public ResponseEntity<?> createCourseRelationship(@RequestBody CourseRelationshipRequest relationship) {
        return BaseResponse.created(courseService.insertCourseRelationship(relationship));
    }

    @PatchMapping("/update-course-relationship")
    public ResponseEntity<?> updateCourseRelationship(@RequestBody CourseRelationship relationship) {
        return null;
        // Todo
    }
}

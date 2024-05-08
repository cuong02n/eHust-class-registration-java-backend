package vn.edu.hust.ehustclassregistrationjavabackend.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.CourseRelationshipRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.StudentCourseRegistrationRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final MetadataService metadataService;


    @GetMapping("/course-relationship")
    public ResponseEntity<?> getRelationship(@RequestParam long relationshipId) {
        return BaseResponse.ok(courseService.getRelationshipById(relationshipId), "Not found relationship");
    }

    @GetMapping("/register-course")
    public ResponseEntity<?> getRegistedCourse(@RequestParam(required = false) String semester) {
        if(semester==null){
            return BaseResponse.ok(courseService.getRegistedCourse());
        }
        return BaseResponse.ok(courseService.getRegistedCourse(semester));
    }

    @PostMapping("/register-course")
    public ResponseEntity<?> registerCourse(@RequestBody StudentCourseRegistrationRequest request) {
        return BaseResponse.created(courseService.registerCourse(request.getCourseIds()));
    }

    @DeleteMapping("/register-course")
    public ResponseEntity<?> unregisterCourse(@RequestBody StudentCourseRegistrationRequest request) {
        return BaseResponse.deleted(courseService.unregisterCourse(request.getCourseIds())+" course(s) unregisted.");
    }
}

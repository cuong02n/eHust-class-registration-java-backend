package vn.edu.hust.ehustclassregistrationjavabackend.controller.common;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/common/classes")
@PreAuthorize("hasAnyRole('STUDENT') or hasAnyRole('ADMIN') or hasAnyRole('SUPER_ADMIN')")
public class CommonClassController {
    private final ClassService classService;
    @GetMapping("/get-by-course-id")
    public ResponseEntity<?> getClassesByCourseIdAndSemester(@RequestParam String semester, @RequestParam String courseId){
        return BaseResponse.ok(classService.getClassByCourseId(courseId,semester,true));
    }

    @GetMapping("/get-all-class")
    public ResponseEntity<?> getAllClassesBySemester(@RequestParam String semester){
        return BaseResponse.ok(classService.getClassBySemester(semester));
    }
}

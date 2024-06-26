package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.ClassCreateRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

import java.io.IOException;

@RestController
@RequestMapping("/admin/class")
@RequiredArgsConstructor
public class AdminClassController {
    final CourseService courseService;
    private final ClassService classService;

    @PostMapping()
    public ResponseEntity<?> createClass(@RequestBody ClassCreateRequest request){
        return BaseResponse.ok(classService.createClass(request.getClasses()));
    }

    @PostMapping("/post-class-by-file")
    public ResponseEntity<?> batchClassByExcel(@RequestBody MultipartFile file) throws IOException {
        return BaseResponse.ok(classService.updateClasses(file));
    }

    @PostMapping("/cancel-class")
    public ResponseEntity<?> cancelClass(@RequestBody ClassPK classPK){
        return BaseResponse.ok(classService.cancelClass(classPK));
    }
}

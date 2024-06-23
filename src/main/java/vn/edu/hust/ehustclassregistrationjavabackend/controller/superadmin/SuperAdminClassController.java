package vn.edu.hust.ehustclassregistrationjavabackend.controller.superadmin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.ClassCreateRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
@RequestMapping("/super-admin/classes")
public class SuperAdminClassController {
    final CourseService courseService;
    final ClassService classService;

    @PostMapping()
    public ResponseEntity<?> createClass(@RequestBody ClassCreateRequest request){
        return BaseResponse.ok(classService.createClass(request.getClasses()));
    }

    @PostMapping("/post-class-by-file")
    public ResponseEntity<?> batchClassByExcel(@RequestBody MultipartFile file) throws IOException {
        return BaseResponse.ok(classService.updateClassesByFile(file));
    }

    @PostMapping("/cancel-class")
    public ResponseEntity<?> cancelClass(@RequestBody ClassPK classPK){
        return BaseResponse.ok(classService.cancelClass(classPK));
    }

}
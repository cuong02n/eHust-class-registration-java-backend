package vn.edu.hust.ehustclassregistrationjavabackend.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {
    final ClassService classService;

//    @PostMapping()
//    public ResponseEntity<?> createClass(@RequestBody ClassDto request) {
//        return BaseResponse.created(classService.createClass(request), "Cannot create class, maybe duplicate the primary key: {id,semester}: {", request.getId(), " and ", request.getSemester(), " }");
//    }

//    @PatchMapping()
//    public ResponseEntity<?> updateClass(@RequestBody ClassDto request){
//
//    }

    @GetMapping()
    public ResponseEntity<?> getClass(@RequestParam String id, @RequestParam String semester) {
        System.out.println("ok");
        return BaseResponse.ok(classService.getClassByIdAndSemester(id, semester).toClassDto(), "Not found class");
    }

    @GetMapping("/get-by-course-id")
    public ResponseEntity<?> getClassByCourseId(@RequestParam String courseId, @RequestParam String semester) {
        return BaseResponse.created(classService.getClassByCourseId(courseId, semester));
    }

    @PostMapping("/register-class")
    public ResponseEntity<?> registerClass(@RequestBody ClassPK classPK){
        return BaseResponse.created(classService.registerClass(classPK),"Lỗi: ban chưa đăng ký được");
    }
}

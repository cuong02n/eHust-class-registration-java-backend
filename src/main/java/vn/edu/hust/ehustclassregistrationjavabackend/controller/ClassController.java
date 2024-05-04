package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {
    final ClassService classService;

//    @GetMapping("/get-by-course-id")
//    public ResponseEntity<List<Class>> getClassByCourseId(String courseId) {
//        return ResponseEntity.ok().body(classRepository.findAllByCourseId(courseId));
//    }

    //    @PutMapping()
//    public ResponseEntity<?> updateClass(@RequestBody Class newClass) {
//        try {
//            classRepository.save(newClass);
//            return ResponseEntity.created(URI.create("/api/class?classId=")).body(newClass);
//        }catch (Exception e){
//            return ResponseEntity.status(409).body(e.getCause().getMessage());
//        }
//    }
    @PostMapping()
    public ResponseEntity<?> createClass(@RequestBody ClassRequest request) {
        return BaseResponse.created(classService.createClass(request), "Cannot create class, Duplicated or Wrong format");
    }

    @GetMapping()
    public ResponseEntity<?> getClass(@RequestParam String id, @RequestParam String semester) {
        return BaseResponse.ok(classService.getClassByIdAndSemester(id,semester),"Not found class");
    }
}

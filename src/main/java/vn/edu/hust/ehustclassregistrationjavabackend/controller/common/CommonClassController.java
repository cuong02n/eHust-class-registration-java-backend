package vn.edu.hust.ehustclassregistrationjavabackend.controller.common;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/classes")
public class CommonClassController {
    final ClassService classService;

    @GetMapping()
    public ResponseEntity<?> getClass(@RequestParam String id, @RequestParam String semester) {
        return BaseResponse.ok(classService.getClassByIdAndSemester(id, semester).toClassDto(), "Not found class");
    }

    @GetMapping("/get-count-registered")
    public ResponseEntity<?> getCountRegisteredClass(@RequestParam @Size(min = 1,max = 20) List<String> classIds, @RequestParam String semester){
        return BaseResponse.ok(classService.countRegisteredOfClass(classIds,semester));
    }

    @GetMapping("/count-all")
    public ResponseEntity<?> getCountRegisteredClass(@RequestParam String semester){
        return BaseResponse.ok(classService.countAllRegisteredOfSemester(semester));
    }


    @GetMapping("/get-by-semester")
    public ResponseEntity<?> getClassBySemester(@RequestParam String semester) {
        return BaseResponse.ok(classService.getClassBySemester(semester));
    }
}

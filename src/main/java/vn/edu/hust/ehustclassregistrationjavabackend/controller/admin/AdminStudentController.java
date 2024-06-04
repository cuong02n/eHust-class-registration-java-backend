package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.common.ClassRegisterRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/student")
public class AdminStudentController {
    final ClassService classService;
    @GetMapping("/get-student-registed")
    public ResponseEntity<?> getStudentRegisted(@RequestParam String semester, @RequestParam String studentEmail){
        return BaseResponse.ok(classService.getRegistedClassByEmailAndSemester(studentEmail,semester));
    }

    @PostMapping("/register-for-student")
    public ResponseEntity<?> registerClassForStudent(@RequestBody ClassRegisterRequest classRegisterRequest){
        return null;
        //TODO:
    }
}

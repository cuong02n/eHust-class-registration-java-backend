package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

import java.security.Signature;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class Controller {
    final UserService
     userService;
    @GetMapping()
    public String test(@RequestParam int x) throws Exception {
        return userService.test(x);
    }
    @PostMapping()
    public String postTest(@RequestBody ClassPK pk){
        return userService.test2(pk);
    }
}

package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Controller {
    @GetMapping()
    public String test(){
        return "Hello World";
    }
}

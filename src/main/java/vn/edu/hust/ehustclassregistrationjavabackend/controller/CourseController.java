package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final MetadataService metadataService;
    @GetMapping("/get-all-metadata")
    public ResponseEntity<List<Metadata>> getAllMetadata(){
        Gson gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String data = gson.toJson(metadataService.getAllMetadata());
        System.out.println(data);
        JsonArray jsonArray = gson.fromJson(data,JsonArray.class);
        System.out.println(jsonArray);
        return ResponseEntity.ok().body(metadataService.getAllMetadata());
    }
}

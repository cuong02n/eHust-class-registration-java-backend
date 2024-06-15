package vn.edu.hust.ehustclassregistrationjavabackend.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;

@RestController
@RequestMapping("public/metadata")
@RequiredArgsConstructor
public class PublicMetadataController {
    final MetadataService metadataService;
    @GetMapping("/get-day-start-year")
    public ResponseEntity<?> getDayStartYear(@RequestParam @NonNull Integer year) {
        return BaseResponse.ok(metadataService.getDayStartWeek1(year));
    }
}

package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.MetadataRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/metadata")
public class AdminMetadataController {
    final MetadataService metadataService;

    @PatchMapping("")
    public ResponseEntity<?> updateMetadata(@RequestBody MetadataRequest request) {
        return BaseResponse.ok(metadataService.updateMetadata(request));
    }

}

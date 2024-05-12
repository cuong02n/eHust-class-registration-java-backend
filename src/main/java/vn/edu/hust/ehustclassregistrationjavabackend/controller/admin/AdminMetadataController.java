package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

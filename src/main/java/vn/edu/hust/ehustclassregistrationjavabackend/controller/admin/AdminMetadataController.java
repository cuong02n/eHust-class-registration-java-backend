package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.MetadataRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/metadata")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminMetadataController {
    final MetadataService metadataService;

    @PatchMapping("/start-year")
    public ResponseEntity<?> updateMetadata(@RequestBody MetadataRequest request) {
        return BaseResponse.ok(metadataService.updateMetadata(request));
    }

}

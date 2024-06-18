package vn.edu.hust.ehustclassregistrationjavabackend.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.superadmin.SuperAdminMetadataRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/metadata")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminMetadataController {
    final MetadataService metadataService;

    @PostMapping("")
    public ResponseEntity<?> updateMetadata(@RequestBody @Valid SuperAdminMetadataRequest rq) {
        return BaseResponse.ok(metadataService.updateMetadata(rq.getKey(), rq.getSemester(), rq.getValue()));
    }
}

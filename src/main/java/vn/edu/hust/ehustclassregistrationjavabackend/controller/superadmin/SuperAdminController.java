package vn.edu.hust.ehustclassregistrationjavabackend.controller.superadmin;

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

@PreAuthorize("hasRole('SUPER_ADMIN')")
@RestController
@RequestMapping("/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {
    private final MetadataService metadataService;

    @PostMapping("/update-metadata")
    public ResponseEntity<?> updateMetadata(@RequestBody SuperAdminMetadataRequest rq){
        return BaseResponse.ok(metadataService.updateMetadata(rq.getKey(),rq.getSemester(),rq.getValue()));
    }
}
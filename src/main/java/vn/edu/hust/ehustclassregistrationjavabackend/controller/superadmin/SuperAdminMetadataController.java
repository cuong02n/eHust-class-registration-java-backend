package vn.edu.hust.ehustclassregistrationjavabackend.controller.superadmin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.superadmin.SuperAdminMetadataRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;

@PreAuthorize("hasRole('SUPER_ADMIN')")
@RestController
@RequestMapping("/super-admin/metadata")
@RequiredArgsConstructor
public class SuperAdminMetadataController {
    private final MetadataService metadataService;

    @PostMapping("")
    public ResponseEntity<?> updateMetadata(@RequestBody SuperAdminMetadataRequest rq){
        return BaseResponse.ok(metadataService.updateMetadata(rq.getMetadataKey(),rq.getSemester(),rq.getValue()));
    }

}

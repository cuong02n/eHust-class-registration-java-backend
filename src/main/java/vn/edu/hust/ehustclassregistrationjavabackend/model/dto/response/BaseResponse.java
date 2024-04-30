package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public abstract class BaseResponse extends ResponseEntity<BaseResponse> {
    private BaseResponse(BaseResponse body, HttpStatusCode status) {
        super(body, status);
    }
}

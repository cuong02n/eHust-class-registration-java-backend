package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response;

import lombok.Builder;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Optional;

public class BaseResponse extends ResponseEntity<BaseResponse> {

    public BaseResponse(BaseResponse body, HttpStatusCode status) {
        super(body, status);
    }

    public static <T> ResponseEntity<?>  createResponse(Optional<?> value,Exception ex,int statusCode){
        if(value.isPresent()){
            return ResponseEntity.status(statusCode).body(value.get());
        }
        if(ex==null){
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}

package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import java.io.Serializable;

/**
 * empty value param meaning error
 */
public class BaseResponse {


    public static <T extends Serializable> ResponseEntity<?> createBaseResponse(T value, int statusCodeSuccess, int statusIfError, String messageIfError) {
        if (value != null) {
            SuccessResponse response = new SuccessResponse(value);
            return ResponseEntity.status(statusCodeSuccess).body(response);
        }
        return ResponseEntity.status(statusIfError).body(new ErrorResponse(statusIfError, messageIfError));
    }

    public static <T extends Serializable> ResponseEntity<?> created(T value, String messageIfError) {
        return createBaseResponse(value, 201, 409, messageIfError);
    }

    public static <T extends Serializable> ResponseEntity<?> ok(T value, String messageIfError) {
        return createBaseResponse(value, 200, 204, messageIfError);
    }

    @AllArgsConstructor
    @Getter
    public static class ErrorResponse {
        @Expose
        int error;
        @Expose
        String message;
    }

    @Getter
    public static class SuccessResponse {
        @Expose
        int error = 0;
        @Expose
        JsonElement data;

        public SuccessResponse(JsonElement data) {
            this.data = data;
        }
        public SuccessResponse(Serializable data){
            this.data = GsonUtil.gsonExpose.toJsonTree(data);
        }
    }
}

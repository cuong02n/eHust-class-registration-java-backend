package vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

/**
 * empty value param meaning error
 */
public class BaseResponse {

    public static <T> ResponseEntity<?> createBaseResponse(T value, int statusCodeSuccess, int statusIfError, String... messageIfError) {
        if (value != null) {
            SuccessResponse<T> response = new SuccessResponse<T>(value);
            return ResponseEntity.status(statusCodeSuccess).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        StringBuilder builder = new StringBuilder();
        for (String message : messageIfError) {
            builder.append(message);
        }
        return ResponseEntity.status(statusIfError).contentType(MediaType.APPLICATION_JSON).body(new ErrorResponse(statusIfError, builder.toString()));
    }

    public static <T> ResponseEntity<?> created(T value, String... messageIfError) {
        return createBaseResponse(value, 201, 204, messageIfError);
    }

    public static <T> ResponseEntity<?> ok(T value, String... messageIfError) {
        return createBaseResponse(value, 200, 404, messageIfError);
    }

    public static <T> ResponseEntity<?> deleted(T value, String... messageIfError) {
        return createBaseResponse(value, 204, 404, messageIfError);
    }

    public static <T> ResponseEntity<?> noContent(String message){
        return createBaseResponse(message, 204, 404);
    }


    @AllArgsConstructor
    @Getter
    public static class ErrorResponse {
        @Expose
        int error;
        @Expose
        String message;

        @Override
        public String toString() {
            return GsonUtil.gsonExpose.toJson(this);
        }
    }

    @Getter
    public static class SuccessResponse<T> {
        @Expose
        int error = 0;
        @Expose
        JsonElement data;

        public SuccessResponse(JsonElement data) {
            this.data = data;
        }

        public SuccessResponse(T data) {
            this(GsonUtil.gsonExpose.toJsonTree(data));
        }
    }
}

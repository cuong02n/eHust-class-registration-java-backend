package vn.edu.hust.ehustclassregistrationjavabackend.config;

import org.springframework.http.HttpStatus;

public class MessageException extends RuntimeException {
    HttpStatus httpStatus;

    public MessageException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public MessageException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }
}

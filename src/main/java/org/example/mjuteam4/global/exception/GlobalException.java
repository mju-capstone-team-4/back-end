package org.example.mjuteam4.global.exception;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Getter
public class GlobalException extends RuntimeException {

    private ExceptionCode exceptionCode;
    public GlobalException(String message, ExceptionCode errorCode) {
        super(message);
        this.exceptionCode = errorCode;
    }

    public GlobalException(ExceptionCode errorCode) {
        super(errorCode.getMessage());
        this.exceptionCode = errorCode;
    }
}

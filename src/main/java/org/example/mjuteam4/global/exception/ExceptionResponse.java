package org.example.mjuteam4.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private final int status;

    private final String code;

    private final String message;

    public static ExceptionResponse from(ExceptionCode errorCode) {
        return new ExceptionResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }
}

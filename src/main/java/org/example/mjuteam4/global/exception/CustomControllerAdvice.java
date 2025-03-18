package org.example.mjuteam4.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {


    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(GlobalException ex) {
        ExceptionCode errorCode = ex.getExceptionCode(); // 예외에서 ErrorCode 추출
        ExceptionResponse errorResponse = ExceptionResponse.from(errorCode); // ErrorResponse 생성
        return ResponseEntity
                .status(errorCode.getStatus()) // HTTP 상태 코드 설정
                .body(errorResponse); // ErrorResponse 반환
    }
}

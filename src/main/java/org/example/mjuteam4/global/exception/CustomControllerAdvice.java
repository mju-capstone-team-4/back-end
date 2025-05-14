package org.example.mjuteam4.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

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

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartException(MultipartException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("잘못된 multipart/form-data 요청입니다.");
    }
}

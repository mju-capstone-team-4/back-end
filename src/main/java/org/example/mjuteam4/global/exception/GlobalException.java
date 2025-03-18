package org.example.mjuteam4.global.exception;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Getter
public class GlobalException extends RuntimeException {

    private ExceptionCode exceptionCode;


    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(GlobalException ex) {
        ExceptionCode exceptionCode = ex.getExceptionCode(); // 예외에서 ErrorCode 추출
        ExceptionResponse exceptionResponse = ExceptionResponse.from(exceptionCode); // ErrorResponse 생성
        return ResponseEntity
                .status(exceptionCode.getStatus()) // HTTP 상태 코드 설정
                .body(exceptionResponse); // ErrorResponse 반환
    }
}

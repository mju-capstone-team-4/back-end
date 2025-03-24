package org.example.mjuteam4.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    // member
    MEMBER_NOT_FOUND(404, "MEMBER_404", "사용자를 찾을 수 없음"),

    // auth
    TOKEN_INVALID(401, "A001", "검증된 토큰이 아닙니다"),
    JWT_SIGNATURE_INVALID(401, "A002", "서명이 검증되지 않음"),
    JwtAuthenticationException(401, "A003", "인증 관련 예외"),
    ILLEGAL_REGISTRATION_ID(401, "A004", "잘못된 id"),
    ;

    private final int status;
    private final String code;
    private final String message;
}

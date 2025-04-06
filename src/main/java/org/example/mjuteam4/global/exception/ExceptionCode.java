package org.example.mjuteam4.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    // member
    MEMBER_NOT_FOUND(404, "MEMBER_001", "사용자를 찾을 수 없음"),
    MEMBER_ALREADY_EXISTS(400, "MEMBER_002", "이미 가입된 유저입니다"),

    // auth
    AUTH_TOKEN_INVALID(401, "AUTH_TOKEN_INVALID", "유효하지 않은 토큰입니다."),
    AUTH_JWT_SIGNATURE_INVALID(401, "AUTH_JWT_SIGNATURE_INVALID", "JWT 서명이 유효하지 않습니다."),
    AUTH_JWT_AUTHENTICATION_FAILED(401, "AUTH_JWT_AUTHENTICATION_FAILED", "JWT 인증 실패"),
    AUTH_ILLEGAL_REGISTRATION_ID(401, "AUTH_ILLEGAL_REGISTRATION_ID", "잘못된 Registration ID입니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}

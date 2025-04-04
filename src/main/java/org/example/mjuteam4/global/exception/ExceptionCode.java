package org.example.mjuteam4.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    //question
    QUESTION_NOT_FOUND(404, "QUESTION_NOT_FOUND","질문을 찾을 수 없습니다"),

    //comment
    COMMENT_NOT_FOUND(404,"COMMENT_NOT_FOUND","댓글을 찾을 수 없습니다"),
    COMMENT_UNAUTHORIZED_EXCEPTION(403, "COMMENT_UNAUTHORIZED_EXCEPTION", "댓글을 수정할 수 있는 권한이 없습니다."),

    //commentLike
    ALREADY_LIKED_EXCEPTION(409,"ALREADY_LIKED_EXCEPTION", "이미 좋아요를 누른 상태에서 다시 좋아요르 누를 수는 없습니다."),
    NOT_LIKED_YET_EXCEPTION(400, "NOT_LIKE_YET_EXCEPTION","좋아요를 누르지 않은 상태에서 좋아요를 취소할 수는 없습니다."),


    //tradePost
    TRADE_POST_NOT_FOUND(404,"TRADE_POST_NOT_FOUND","해당 거래 게시글을 찾을 수 없습니다"),
    // member
    MEMBER_NOT_FOUND(404, "MEMBER_001", "사용자를 찾을 수 없음"),
    MEMBER_ALREADY_EXISTS(400, "MEMBER_002", "이미 가입된 유저입니다"),

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

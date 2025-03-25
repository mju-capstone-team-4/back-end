package org.example.mjuteam4.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    MEMBER_NOT_FOUND(404, "MEMBER_404", "사용자를 찾을 수 없음"),

    //question
    QUESTION_NOT_FOUND(404, "QUESTION_NOT_FOUND","질문을 찾을 수 없습니다"),
    ;



    private final int status;
    private final String code;
    private final String message;
}

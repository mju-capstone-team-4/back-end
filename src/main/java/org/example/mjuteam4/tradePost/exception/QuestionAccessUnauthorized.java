package org.example.mjuteam4.tradePost.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class QuestionAccessUnauthorized extends GlobalException {

    public QuestionAccessUnauthorized() {
        super(ExceptionCode.COMMENT_UNAUTHORIZED_EXCEPTION);
    }
}

package org.example.mjuteam4.question.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class QuestionNotFound extends GlobalException {
    public QuestionNotFound() {
        super(ExceptionCode.QUESTION_NOT_FOUND);
    }
}

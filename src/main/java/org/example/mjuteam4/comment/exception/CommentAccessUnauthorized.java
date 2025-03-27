package org.example.mjuteam4.comment.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class CommentAccessUnauthorized extends GlobalException {

    public CommentAccessUnauthorized() {
        super(ExceptionCode.COMMENT_UNAUTHORIZED_EXCEPTION);
    }
}

package org.example.mjuteam4.comment.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class CommentNotFound extends GlobalException {
    public CommentNotFound() {
        super(ExceptionCode.COMMENT_NOT_FOUND);
    }
}

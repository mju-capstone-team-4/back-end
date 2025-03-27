package org.example.mjuteam4.commentLike.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class AlreadyLikedException extends GlobalException {
    public AlreadyLikedException() {
        super(ExceptionCode.ALREADY_LIKED_EXCEPTION);
    }
}

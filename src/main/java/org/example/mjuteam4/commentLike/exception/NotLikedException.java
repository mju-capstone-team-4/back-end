package org.example.mjuteam4.commentLike.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class NotLikedException extends GlobalException {
    public NotLikedException() {
        super(ExceptionCode.NOT_LIKED_YET_EXCEPTION);
    }
}

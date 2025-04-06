package org.example.mjuteam4.redis.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class TokenInvalidException extends GlobalException {

    public TokenInvalidException() {
        super(ExceptionCode.AUTH_TOKEN_INVALID);
    }
}

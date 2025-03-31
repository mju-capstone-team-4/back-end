package org.example.mjuteam4.redis.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class JwtSignatureInvalidException extends GlobalException {
    public JwtSignatureInvalidException() {
        super(ExceptionCode.JWT_SIGNATURE_INVALID);
    }
}

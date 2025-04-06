package org.example.mjuteam4.security.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class JwtAuthenticationException extends GlobalException {
    public JwtAuthenticationException() {
        super(ExceptionCode.AUTH_JWT_AUTHENTICATION_FAILED);
    }
}

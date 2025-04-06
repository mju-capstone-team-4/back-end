package org.example.mjuteam4.mypage.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class IllegalRegistrationIdException extends GlobalException {
    public IllegalRegistrationIdException() {
        super(ExceptionCode.ILLEGAL_REGISTRATION_ID);
    }
}

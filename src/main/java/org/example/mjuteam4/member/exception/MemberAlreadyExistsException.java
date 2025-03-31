package org.example.mjuteam4.member.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class MemberAlreadyExistsException extends GlobalException {
    public MemberAlreadyExistsException() {
        super(ExceptionCode.MEMBER_ALREADY_EXISTS);
    }
}

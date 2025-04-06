package org.example.mjuteam4.mypage.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class MemberNotFoundException extends GlobalException {

    public MemberNotFoundException() {
        super(ExceptionCode.MEMBER_NOT_FOUND);
    }
}

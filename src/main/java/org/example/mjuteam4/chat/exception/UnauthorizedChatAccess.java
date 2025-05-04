package org.example.mjuteam4.chat.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class UnauthorizedChatAccess extends GlobalException {
    public UnauthorizedChatAccess() {
        super(ExceptionCode.UNAUTHORIZED_CHAT_ACCESS);
    }
}

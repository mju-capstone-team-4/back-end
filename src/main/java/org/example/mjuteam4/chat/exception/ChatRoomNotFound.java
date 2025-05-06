package org.example.mjuteam4.chat.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class ChatRoomNotFound extends GlobalException {
    public ChatRoomNotFound() {
        super(ExceptionCode.CHATROOM_NOT_FOUND);
    }
}

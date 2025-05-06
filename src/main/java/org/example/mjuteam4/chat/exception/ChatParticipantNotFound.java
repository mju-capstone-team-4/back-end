package org.example.mjuteam4.chat.exception;

import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;

public class ChatParticipantNotFound extends GlobalException {
    public ChatParticipantNotFound() {super(ExceptionCode.CHAT_PARTICIPANT_NOT_FOUND);}
}

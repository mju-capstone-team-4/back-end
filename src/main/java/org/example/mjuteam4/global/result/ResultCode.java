package org.example.mjuteam4.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {


    ;

    private final int status;
    private final String code;
    private final String message;
}

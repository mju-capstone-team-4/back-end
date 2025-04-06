package org.example.mjuteam4.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    REGISTER_MYPLANT_SUCCESS(200, "MP001", "내 식물 등록 성공"),

    ;

    private final int status;
    private final String code;
    private final String message;
}

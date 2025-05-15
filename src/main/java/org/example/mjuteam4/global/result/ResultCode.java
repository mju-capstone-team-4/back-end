package org.example.mjuteam4.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // plant
    REGISTER_MYPLANT_SUCCESS(200, "REGISTER_MYPLANT_SUCCESS", "내 식물 등록 성공"),
    DELETE_MYPLANT_SUCCESS(200, "DELETE_MYPLANT_SUCCESS", "내 식물 삭제 성공"),
    UPDATE_CYCLING_SUCCESS(200, "UPDATE_CYCLING_SUCCESS", "내 식물 주기 등록 성공"),

    // member
    DELETE_MEMBER_SUCCESS(200, "DELETE_MEMBER_SUCCESS", "회원 탈퇴 성공"),
    UPDATE_MY_INFO_SUCCESS(200, "UPDATE_MY_INFO_SUCCESS", "내 정보 변경 성공"),
    REGISTER_MEMBER_PROFILE(200, "REGISTER_MEMBER_PROFILE", "회원 사진 등록 성공"),
    ;

    private final int status;
    private final String code;
    private final String message;
}

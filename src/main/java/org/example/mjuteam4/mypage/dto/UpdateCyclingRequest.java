package org.example.mjuteam4.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateCyclingRequest {

    private Integer waterCycle; // 물주는 주기

    private Integer repottingCycle; // 분갈이 주기

    private Integer fertilizingCycle; // 비료 주기
}

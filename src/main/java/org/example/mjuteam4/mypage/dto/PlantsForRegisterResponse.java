package org.example.mjuteam4.mypage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlantsForRegisterResponse {

    private Long plantId;

    private String name;

    private String plantPilbkNo; // 도감번호 (PK)

}

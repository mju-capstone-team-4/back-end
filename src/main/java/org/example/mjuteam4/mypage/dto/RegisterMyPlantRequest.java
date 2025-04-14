package org.example.mjuteam4.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterMyPlantRequest {

    private String name;

    private String description;

    private Long plantId;
}

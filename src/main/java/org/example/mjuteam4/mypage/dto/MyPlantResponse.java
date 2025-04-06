package org.example.mjuteam4.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.mjuteam4.mypage.entity.MyPlant;

@Builder
@Getter
public class MyPlantResponse {

    private String name;

    private String description;

    public static MyPlantResponse from(MyPlant myPlant) {
        return MyPlantResponse.builder()
                .name(myPlant.getName())
                .description(myPlant.getDescription())
                .build();
    }
}

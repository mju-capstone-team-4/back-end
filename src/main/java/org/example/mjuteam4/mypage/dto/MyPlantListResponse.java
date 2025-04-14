package org.example.mjuteam4.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.mjuteam4.mypage.entity.MyPlant;

@Builder
@Getter
public class MyPlantListResponse {

    private Long id;

    private String name;

    private String description;

    public static MyPlantListResponse from(MyPlant myPlant) {
        return MyPlantListResponse.builder()
                .id(myPlant.getId())
                .name(myPlant.getName())
                .description(myPlant.getDescription())
                .build();
    }
}

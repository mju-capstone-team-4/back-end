package org.example.mjuteam4.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.mjuteam4.mypage.entity.MyPlant;

@Builder
@Getter
public class MyPlantListResponse {

    private Long myPlantId;

    private String name;

    private String description;

    private String sampleImageUrl;

    public static MyPlantListResponse from(MyPlant myPlant) {
        return MyPlantListResponse.builder()
                .myPlantId(myPlant.getId())
                .name(myPlant.getName())
                .description(myPlant.getDescription())
                .sampleImageUrl(myPlant.getImageUrl())
                .build();
    }
}

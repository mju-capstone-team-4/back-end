package org.example.mjuteam4.myPlant.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.myPlant.MyPlant;

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

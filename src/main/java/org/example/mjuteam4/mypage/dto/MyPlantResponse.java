package org.example.mjuteam4.mypage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MyPlantResponse {

    private Long plantId;

    private String plantName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private List<LocalDate> wateringDates;

    private List<LocalDate> repottingDates;

    private List<LocalDate> fertilizingDates;

}

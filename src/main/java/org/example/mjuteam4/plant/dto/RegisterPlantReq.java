package org.example.mjuteam4.plant.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterPlantReq {

    private String fertilizingCycle;
    private String repottingCycle;
    private String waterCycle;

    private String description;
    private String familyKorNm;
    private String genusKorNm;
    private String imgUrl;

    private String plantGnrlNm;
    private String plantPilbkNo;
    private String plantSpecsScnm;

    private String dstrb;
    private String flwrDesc;
    private String fritDesc;
    private String grwEvrntDesc;

    private String leafDesc;
    private String shpe;
    private String sz;
    private String useMthdDesc;
}

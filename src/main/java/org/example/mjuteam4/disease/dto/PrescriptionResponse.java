package org.example.mjuteam4.disease.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrescriptionResponse{
    private String diseaseInfo;
    private String watering;
    private String environment;
    private String nutrition;

//    private PrescriptionResponse(DiseaseResponse diseaseResponse){
//        this.result = diseaseResponse.getResult();
//        this.confidence = diseaseResponse.getConfidence();
//    }
//    public PrescriptionResponse addDiseaseInfo(DiseaseResponse diseaseResponse){
//        return new PrescriptionResponse(diseaseResponse);
//    }
}

package org.example.mjuteam4.disease.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDiseaseResponse {
    private String result;
    private Double confidence;
    private String diseaseInfo;
    private String watering;
    private String environment;
    private String nutrition;
    private ClientDiseaseResponse(DiseaseResponse diseaseResponse, PrescriptionResponse prescriptionResponse){
        this.result = diseaseResponse.getResult();
        this.confidence = diseaseResponse.getConfidence();

        this.diseaseInfo = prescriptionResponse.getDiseaseInfo();
        this.watering = prescriptionResponse.getWatering();
        this.environment = prescriptionResponse.getEnvironment();
        this.nutrition = prescriptionResponse.getNutrition();
    }
    public static ClientDiseaseResponse createWith(DiseaseResponse diseaseResponse, PrescriptionResponse prescriptionResponse){
        return new ClientDiseaseResponse(diseaseResponse, prescriptionResponse);
    }
}

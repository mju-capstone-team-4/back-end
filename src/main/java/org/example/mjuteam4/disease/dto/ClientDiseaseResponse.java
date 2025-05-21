package org.example.mjuteam4.disease.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.disease.dto.aiServer.AiServerResponse;
import org.example.mjuteam4.disease.dto.gpt.GptDiseaseResponse;
import org.example.mjuteam4.disease.entity.Disease;

@Data
@NoArgsConstructor
public class ClientDiseaseResponse {
    private Long diseaseId;
    private String result;
    private Double confidence;
    private String diseaseInfo;
    private String watering;
    private String environment;
    private String nutrition;
    private ClientDiseaseResponse(Disease disease){
        this.diseaseId = disease.getId();
        this.result = disease.getResult();
        this.confidence = disease.getConfidence();

        this.diseaseInfo = disease.getDiseaseInfo();
        this.watering = disease.getWatering();
        this.environment = disease.getEnvironment();
        this.nutrition = disease.getNutrition();
    }
    public static ClientDiseaseResponse createWith(Disease disease){
        return new ClientDiseaseResponse(disease);
    }
}

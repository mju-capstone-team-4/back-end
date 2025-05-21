package org.example.mjuteam4.disease.dto.gpt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GptDiseaseResponse {
    private String diseaseInfo;
    private String watering;
    private String environment;
    private String nutrition;
}

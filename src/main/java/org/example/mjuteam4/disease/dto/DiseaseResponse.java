package org.example.mjuteam4.disease.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiseaseResponse {
    private String result;
    private Double confidence;
}

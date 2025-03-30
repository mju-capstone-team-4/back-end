package org.example.mjuteam4.disease.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PredictResponse {
    private String prediction;
    private Double confidence;
}

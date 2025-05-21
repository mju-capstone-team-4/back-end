package org.example.mjuteam4.disease.dto.aiServer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AiServerResponse {
    private String result;
    private Double confidence;
}

package org.example.mjuteam4.disease.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.disease.dto.aiServer.AiServerResponse;
import org.example.mjuteam4.disease.dto.gpt.GptDiseaseResponse;
import org.example.mjuteam4.mypage.entity.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Disease {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diseaseId")
    private Long id;
    private String result;
    private Double confidence;
    private String diseaseInfo;
    private String watering;
    private String environment;
    private String nutrition;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //생성자
    private Disease (AiServerResponse aiServerResponse, GptDiseaseResponse gptDiseaseResponse){
        this.result = aiServerResponse.getResult();
        this.confidence = aiServerResponse.getConfidence();
        this.diseaseInfo = gptDiseaseResponse.getDiseaseInfo();
        this.watering = gptDiseaseResponse.getWatering();
        this.environment = gptDiseaseResponse.getEnvironment();
        this.nutrition = gptDiseaseResponse.getNutrition();
    }

    // 생성 메서드
    public static Disease createWith(AiServerResponse aiServerResponse, GptDiseaseResponse gptDiseaseResponse){
        return new Disease(aiServerResponse, gptDiseaseResponse);
    }

}

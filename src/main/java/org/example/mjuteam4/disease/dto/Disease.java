package org.example.mjuteam4.disease.dto;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.mypage.entity.Member;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Disease {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Long Id;
    private String result;
    private Double confidence;
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    // 생성 메서드


}

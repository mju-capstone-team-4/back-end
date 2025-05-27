package org.example.mjuteam4.mypage.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.plant.Plant;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class MyPlant {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private LocalDate lastWateredDate;

    private boolean recommendTonic;

    private Integer waterCycle; // 물주는 주기

    private Integer repottingCycle; // 분갈이 주기

    private Integer fertilizingCycle; // 비료 주기

    private String imageUrl;

    private String sampleImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plant plant;

    @Builder
    public MyPlant(String name, String description, String sampleImageUrl, LocalDate lastWateredDate, boolean recommendTonic, String imageUrl, Member member, Plant plant) {
        this.name = name;
        this.description = description;
        this.lastWateredDate = lastWateredDate;
        this.recommendTonic = recommendTonic;
        this.imageUrl = imageUrl;
        this.member = member;
        this.plant = plant;
        this.sampleImageUrl = sampleImageUrl;
    }

    public void updateCycling(Integer waterCycle, Integer repottingCycle, Integer fertilizingCycle) {
        this.waterCycle = waterCycle;
        this.repottingCycle = repottingCycle;
        this.fertilizingCycle = fertilizingCycle;
    }
}

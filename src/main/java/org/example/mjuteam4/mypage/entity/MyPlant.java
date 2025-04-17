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

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plant plant;

    @Builder
    public MyPlant(String name, String description, LocalDate lastWateredDate, boolean recommendTonic, String imageUrl, Member member, Plant plant) {
        this.name = name;
        this.description = description;
        this.lastWateredDate = lastWateredDate;
        this.recommendTonic = recommendTonic;
        this.imageUrl = imageUrl;
        this.member = member;
        this.plant = plant;
    }
}

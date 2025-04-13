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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plant plant;

    @Builder
    public MyPlant(String name, String description, Member member, Plant plant) {
        this.name = name;
        this.description = description;
        this.plant = plant;
        this.member = member;
    }
}

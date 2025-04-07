package org.example.mjuteam4.mypage.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MyPlant {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public MyPlant(String name, String description, Member member) {
        this.name = name;
        this.description = description;
        this.member = member;
    }
}

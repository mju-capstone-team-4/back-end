package org.example.mjuteam4.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id @GeneratedValue
    public Long id;

    private String email;

    private String username;

    private String profileUrl;

    /**
     * 내 식물 리스트
     *
     */
}

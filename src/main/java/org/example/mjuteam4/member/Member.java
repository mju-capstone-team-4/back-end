package org.example.mjuteam4.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.plant.Plant;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    public Long id;

    private String email;

    private String username;

    private String profileUrl;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    public enum Role{
        ROLE_USER, ROLE_ADMIN
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Plant> plantList = new ArrayList<>();

    public Member(String email, String username, String profileUrl) {
        this.email = email;
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

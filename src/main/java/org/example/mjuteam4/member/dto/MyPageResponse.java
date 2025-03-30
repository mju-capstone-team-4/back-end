package org.example.mjuteam4.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.member.Member;
import org.example.mjuteam4.myPlant.dto.MyPlantResponse;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class MyPageResponse {

    public Long id;

    private String email;

    private String username;

    private String profileUrl;

    private List<MyPlantResponse> plants;

    public static MyPageResponse from(Member member, List<MyPlantResponse> plants) {

        return MyPageResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .profileUrl(member.getProfileUrl())
                .plants(plants)
                .build();

    }

}

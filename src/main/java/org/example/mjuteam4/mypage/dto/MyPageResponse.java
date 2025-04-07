package org.example.mjuteam4.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.mjuteam4.mypage.entity.Member;

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

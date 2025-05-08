package org.example.mjuteam4.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.mypage.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String username;
    private String profileUrl;

    private MemberResponse(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.profileUrl = member.getProfileUrl();
    }
    public static MemberResponse create(Member member){
        return new MemberResponse(member);
    }
}

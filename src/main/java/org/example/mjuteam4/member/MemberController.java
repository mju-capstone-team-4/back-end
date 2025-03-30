package org.example.mjuteam4.member;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.member.dto.MyPageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    // 마이페이지 조회
    @GetMapping("/me")
    public ResponseEntity<MyPageResponse> getMyPage(){
        return ResponseEntity.ok().body(memberService.getMyPage());
    }

    // 내 식물 리스트

    // 마이페이지 조회
}

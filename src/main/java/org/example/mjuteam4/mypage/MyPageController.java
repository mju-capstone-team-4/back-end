package org.example.mjuteam4.mypage;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.result.ResultResponse;
import org.example.mjuteam4.mypage.dto.MyPageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MypageService mypageService;

    // 마이페이지 조회
    @GetMapping()
    public ResponseEntity<MyPageResponse> getMyPage(){
        return ResponseEntity.ok().body(mypageService.getMyPage());
    }

    // 내 식물 등록
    @PostMapping("/plant")
    public ResponseEntity<ResultResponse> registerMyPlant(){
        mypageService.registerMyPlant();

    }
}

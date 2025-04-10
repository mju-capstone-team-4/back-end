package org.example.mjuteam4.mypage;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.result.ResultCode;
import org.example.mjuteam4.global.result.ResultResponse;
import org.example.mjuteam4.mypage.dto.MyPageResponse;
import org.example.mjuteam4.mypage.dto.MyPlantResponse;
import org.example.mjuteam4.mypage.dto.RegisterMyPlantRequest;
import org.example.mjuteam4.mypage.dto.UpdateMyInfoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/myplant")
    public ResponseEntity<ResultResponse> registerMyPlant(@RequestBody RegisterMyPlantRequest registerMyPlantRequest){
        mypageService.registerMyPlant(registerMyPlantRequest);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.REGISTER_MYPLANT_SUCCESS));
    }

    // 내 식물 조회
    @GetMapping("/myplant")
    public ResponseEntity<List<MyPlantResponse>> getMyPlant(){
        return ResponseEntity.ok().body(mypageService.getMyPlant());
    }

    // 내 식물 삭제]
    @DeleteMapping("/myplant/{myPlantId}")
    public ResponseEntity<ResultResponse> deleteMyPlant(@PathVariable Long myPlantId){
        mypageService.deleteMyPlant(myPlantId);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.DELETE_MYPLANT_SUCCESS));
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<ResultResponse> deleteID(){
        mypageService.deleteID();
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.DELETE_MEMBER_SUCCESS));
    }

    // 내 정보 변경
    @PostMapping("/me")
    public ResponseEntity<ResultResponse> updateMyInfo(@RequestBody UpdateMyInfoRequest request) {
        mypageService.updateMyInfo(request);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.UPDATE_MY_INFO_SUCCESS));
    }
}

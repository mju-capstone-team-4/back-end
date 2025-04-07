package org.example.mjuteam4.mypage;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.result.ResultCode;
import org.example.mjuteam4.global.result.ResultResponse;
import org.example.mjuteam4.mypage.dto.MyPageResponse;
import org.example.mjuteam4.mypage.dto.MyPlantResponse;
import org.example.mjuteam4.mypage.dto.RegisterMyPlantRequest;
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

    @GetMapping("/myplant")
    public ResponseEntity<List<MyPlantResponse>> getMyPlant(){
        return ResponseEntity.ok().body(mypageService.getMyPlant());
    }

    @DeleteMapping("/myplant/{myPlantId}")
    public ResponseEntity<ResultResponse> deleteMyPlant(@PathVariable String myPlantId){
        mypageService.deleteMyPlant(myPlantId);
        return ResponseEntity.ok().body(ResultResponse.of(ResultCode.DELETE_MYPLANT_SUCCESS));
    }
}

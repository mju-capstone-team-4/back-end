package org.example.mjuteam4.mypage;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.dto.MyPageResponse;
import org.example.mjuteam4.mypage.entity.MyPlant;
import org.example.mjuteam4.mypage.repository.MyPlantRepository;
import org.example.mjuteam4.mypage.dto.MyPlantResponse;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.mypage.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MyPlantRepository myPlantRepository;

    public MyPageResponse getMyPage() {

        Member loginMember = jwtUtil.getLoginMember();

        List<MyPlant> myPlantList = loginMember.getMyPlantList();
        List<MyPlantResponse> myPlantResponseList = new ArrayList<>();

        for (MyPlant myPlant : myPlantList) {
            MyPlantResponse myPlantResponse = MyPlantResponse.from(myPlant);
            myPlantResponseList.add(myPlantResponse);
        }

        return MyPageResponse.from(loginMember,myPlantResponseList);
    }
}

package org.example.mjuteam4.mypage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.dto.MyPageResponse;
import org.example.mjuteam4.mypage.dto.RegisterMyPlantRequest;
import org.example.mjuteam4.mypage.dto.UpdateMyInfoRequest;
import org.example.mjuteam4.mypage.entity.MyPlant;
import org.example.mjuteam4.mypage.repository.MyPlantRepository;
import org.example.mjuteam4.mypage.dto.MyPlantResponse;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.mypage.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public void registerMyPlant(RegisterMyPlantRequest request) {
        Member loginMember = jwtUtil.getLoginMember();

        MyPlant myplant = MyPlant.builder()
                .member(loginMember)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        myPlantRepository.save(myplant);
    }

    public List<MyPlantResponse> getMyPlant() {

        Member loginMember = jwtUtil.getLoginMember();

        List<MyPlant> myPlantList = loginMember.getMyPlantList();

        List<MyPlantResponse> myPlantResponseList = new ArrayList<>();

        for (MyPlant myPlant : myPlantList) {
            MyPlantResponse myPlantResponse = MyPlantResponse.from(myPlant);
            myPlantResponseList.add(myPlantResponse);
        }

        return myPlantResponseList;
    }

    @Transactional
    public void deleteMyPlant(String myPlantId) {
        MyPlant myPlant = myPlantRepository.findByPlantId(myPlantId)
                .orElseThrow(() -> new GlobalException(ExceptionCode.MY_PLANT_NOT_FOUND));

        myPlantRepository.delete(myPlant);
    }

    @Transactional
    public void deleteID() {
        Member loginMember = jwtUtil.getLoginMember();

        memberRepository.delete(loginMember);
    }

    @Transactional
    public void updateMyInfo(UpdateMyInfoRequest request) {

        Member loginMember = jwtUtil.getLoginMember();

        loginMember.updateMember(
                request.getEmail(),
                request.getUsername()
        );

        memberRepository.save(loginMember);
    }
}

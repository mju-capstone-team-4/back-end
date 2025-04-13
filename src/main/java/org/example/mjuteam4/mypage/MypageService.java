package org.example.mjuteam4.mypage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.exception.ExceptionCode;
import org.example.mjuteam4.global.exception.GlobalException;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.dto.*;
import org.example.mjuteam4.mypage.entity.MyPlant;
import org.example.mjuteam4.mypage.repository.MyPlantRepository;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.mypage.repository.MemberRepository;
import org.example.mjuteam4.plant.Plant;
import org.example.mjuteam4.plant.PlantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MyPlantRepository myPlantRepository;
    private final PlantRepository plantRepository;

    public MyPageResponse getMyPage() {

        Member loginMember = jwtUtil.getLoginMember();

        List<MyPlant> myPlantList = loginMember.getMyPlantList();
        List<MyPlantListResponse> myPlantResponseList = new ArrayList<>();

        for (MyPlant myPlant : myPlantList) {
            MyPlantListResponse myPlantResponse = MyPlantListResponse.from(myPlant);
            myPlantResponseList.add(myPlantResponse);
        }

        return MyPageResponse.from(loginMember,myPlantResponseList);
    }

    @Transactional
    public void registerMyPlant(RegisterMyPlantRequest request) {
        Member loginMember = jwtUtil.getLoginMember();

        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> new GlobalException(ExceptionCode.PLANT_NOT_FOUND));

        MyPlant myplant = MyPlant.builder()
                .member(loginMember)
                .plant(plant)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        loginMember.getMyPlantList().add(myplant);

        myPlantRepository.save(myplant);
    }

    public List<MyPlantListResponse> getMyPlant() {

        Member loginMember = jwtUtil.getLoginMember();

        List<MyPlant> myPlantList = loginMember.getMyPlantList();

        List<MyPlantListResponse> myPlantResponseList = new ArrayList<>();

        for (MyPlant myPlant : myPlantList) {
            MyPlantListResponse myPlantResponse = MyPlantListResponse.from(myPlant);
            myPlantResponseList.add(myPlantResponse);
        }

        return myPlantResponseList;
    }

    @Transactional
    public void deleteMyPlant(Long myPlantId) {
        MyPlant myPlant = myPlantRepository.findById(myPlantId)
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

    public List<PlantsForRegisterResponse> searchPlantByName(String plantName) {

        List<Plant> allByName = plantRepository.findAllByName(plantName);
        List<PlantsForRegisterResponse> plantsForRegisterResponseList = new ArrayList<>();

        for (Plant plant : allByName) {
            PlantsForRegisterResponse plantsForRegisterResponse = PlantsForRegisterResponse.builder()
                    .id(plant.getId())
                    .name(plant.getName())
                    .build();

            plantsForRegisterResponseList.add(plantsForRegisterResponse);
        }

        return plantsForRegisterResponseList;
    }

    public MyPlantResponse getMyPlantSchedule(Long myPlantId) {

        MyPlant myPlant = myPlantRepository.findById(myPlantId)
                .orElseThrow(() -> new GlobalException(ExceptionCode.MY_PLANT_NOT_FOUND));

        List<LocalDate> wateringDates = calculateNextWateringDates(myPlant, 60); // 2개월 정도 커버

        return MyPlantResponse.builder()
                .plantId(myPlant.getId())
                .wateringDates(wateringDates)
                .plantName(myPlant.getName())
                .build();
    }

    private List<LocalDate> calculateNextWateringDates(MyPlant myPlant, int daysAhead) {
        List<LocalDate> result = new ArrayList<>();

        int cycle = myPlant.getPlant().getWaterCyclingDays(); // 항상 Plant에 저장된 주기 사용
        LocalDate nextDate = myPlant.getLastWateredDate().plusDays(cycle); // 마지막 물준날짜 + 주기
        LocalDate endDate = LocalDate.now().plusDays(daysAhead); // 오늘부터 daysAhead(60일) 후까지 커버

        while (!nextDate.isAfter(endDate)) {
            result.add(nextDate);
            nextDate = nextDate.plusDays(cycle);
        }

        return result;
    }
}

package org.example.mjuteam4.mypage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.example.mjuteam4.question.dto.response.QuestionResponse;
import org.example.mjuteam4.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MyPlantRepository myPlantRepository;
    private final PlantRepository plantRepository;
    private final StorageService storageService;


    public Page<MemberResponse> getMemberList(Pageable pageable){
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(MemberResponse::create);
    }

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
    public void registerMyPlant(RegisterMyPlantRequest request, MultipartFile file) {
        Member loginMember = jwtUtil.getLoginMember();

        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> new GlobalException(ExceptionCode.PLANT_NOT_FOUND));

        String imageUrl = "";

        if (file != null && !file.isEmpty()) {
            // 파일 저장 로직
            imageUrl = storageService.uploadFile(file, "myplant", loginMember.getId());

        }
        MyPlant myplant = MyPlant.builder()
                .member(loginMember)
                .plant(plant)
                .name(request.getName())
                .lastWateredDate(LocalDate.now())
                .description(request.getDescription())
                .imageUrl(imageUrl)
                .recommendTonic(request.isRecommendTonic())
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

        List<Plant> allByName = plantRepository.findByPlantGnrlNmContaining(plantName);
        List<PlantsForRegisterResponse> plantsForRegisterResponseList = new ArrayList<>();

        for (Plant plant : allByName) {
            PlantsForRegisterResponse plantsForRegisterResponse = PlantsForRegisterResponse.builder()
                    .id(plant.getId())
                    .name(plant.getPlantGnrlNm())
                    .build();

            plantsForRegisterResponseList.add(plantsForRegisterResponse);
        }

        return plantsForRegisterResponseList;
    }

    @Transactional
    public MyPlantResponse getMyPlantSchedule(Long myPlantId) {

        MyPlant myPlant = myPlantRepository.findById(myPlantId)
                .orElseThrow(() -> new GlobalException(ExceptionCode.MY_PLANT_NOT_FOUND));

        Integer waterCycle = myPlant.getPlant().getWaterCycle();
        Integer repottingCycle = myPlant.getPlant().getRepottingCycle();
        Integer fertilizingCycle = myPlant.getPlant().getFertilizingCycle();

        List<LocalDate> wateringDates = calculateDates(myPlant, waterCycle, 90);
        List<LocalDate> repottingDates = calculateDates(myPlant, repottingCycle, 365);
        List<LocalDate> fertilizingDates = calculateDates(myPlant,fertilizingCycle, 365);
        log.info("wateringDate = {}", wateringDates);// 3개월 정도 커버

        return MyPlantResponse.builder()
                .plantId(myPlant.getId())
                .wateringDates(wateringDates)
                .repottingDates(repottingDates)
                .fertilizingDates(fertilizingDates)
                .plantName(myPlant.getName())
                .build();
    }


    private List<LocalDate> calculateDates(MyPlant myPlant, Integer cycle, int daysAhead) {
        List<LocalDate> result = new ArrayList<>();


        LocalDate nextDate = myPlant.getLastWateredDate().plusDays(cycle);
        LocalDate endDate = LocalDate.now().plusDays(daysAhead);

        while (!nextDate.isAfter(endDate)) {
            result.add(nextDate);
            nextDate = nextDate.plusDays(cycle);
        }

        return result;
    }


}

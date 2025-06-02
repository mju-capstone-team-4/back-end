package org.example.mjuteam4.disease;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.chatbot.service.ChatBotService;
import org.example.mjuteam4.disease.dto.ClientDiseaseResponse;
import org.example.mjuteam4.disease.dto.aiServer.AiServerRequest;
import org.example.mjuteam4.disease.dto.aiServer.AiServerResponse;
import org.example.mjuteam4.disease.dto.gpt.GptDiseaseResponse;
import org.example.mjuteam4.disease.entity.Disease;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.mypage.exception.MemberNotFoundException;
import org.example.mjuteam4.mypage.repository.MemberRepository;
import org.example.mjuteam4.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiseaseService {
    private final ChatBotService chatBotService;
    private final StorageService storageService;
    private final DiseaseRepository diseaseRepository;
    private final MemberRepository memberRepository;
    private static final String AIEC2ADDRESS = "15.164.98.30";
    private final JwtUtil jwtUtil;

    // 단일 파일 업로드 한 후 얻은 이미지 URL을 AI 서버에 전송하여 예측값을 가져온다.

    public ClientDiseaseResponse predict(AiServerRequest aiServerRequest, Long memberId) throws IOException {// 👈 현재 인증 정보 저장


        log.debug("disease service thread: " + Thread.currentThread());
        MultipartFile file = aiServerRequest.getFile();
        String description = aiServerRequest.getDescription();
        String plant = aiServerRequest.getPlant();

        // S3에 이미지 전송
        String s3ImageUrl = storageService.uploadFile(file,"disease", memberId);

        // AI 서버로 전송할 요청 생성
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("image_url", s3ImageUrl);

        // 식물 종류 추가
        log.debug("target crop: {}", plant);
        requestBody.put("crop", plant);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap<String, String>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        // 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        String fastApiUrl = "http://" + AIEC2ADDRESS +  "/predict"; // "http://<EC2-퍼블릭-IP>/predict"
        ResponseEntity<AiServerResponse> response = restTemplate.exchange(fastApiUrl, HttpMethod.POST, requestEntity, AiServerResponse.class);

        // 307 리다이렉트 처리
        if (response.getStatusCode() == HttpStatus.TEMPORARY_REDIRECT) {
            String newUrl = response.getHeaders().getLocation().toString(); // 새로운 URL 가져오기
            response = restTemplate.exchange(newUrl, HttpMethod.POST, requestEntity, AiServerResponse.class);
        }

        log.info("[resposne status code] = {}", response.getStatusCode());

        AiServerResponse aiServerResponse = response.getBody();
        GptDiseaseResponse gptDiseaseResponse = chatBotService.generatePrescription(aiServerResponse.getResult());

        Disease disease = Disease.createWith(aiServerResponse, gptDiseaseResponse);

        Member member = memberRepository.findWithDiseasesById(memberId).orElseThrow(MemberNotFoundException::new);
        member.addDisease(disease);
        diseaseRepository.save(disease);

        return ClientDiseaseResponse.createWith(disease);

    }

    public Page<Disease> getDiseaseRecord(Pageable pageable, Long memberId){
        return diseaseRepository.findByMemberIdOrderByCreatedAtDesc(pageable, memberId);
    }


}

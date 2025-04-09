package org.example.mjuteam4.disease;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.disease.dto.DiseaseRequest;
import org.example.mjuteam4.disease.dto.DiseaseResponse;
import org.example.mjuteam4.storage.StorageService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class DiseaseService {

    private final StorageService storageService;
    private static final String AIEC2ADDRESS = "15.165.160.41";

    // 단일 파일 업로드 한 후 얻은 이미지 URL을 AI 서버에 전송하여 예측값을 가져온다.
    public DiseaseResponse predict(DiseaseRequest diseaseRequest, Long memberId) throws IOException {

        MultipartFile file = diseaseRequest.getFile();
        String description = diseaseRequest.getDescription();
        String plant = diseaseRequest.getPlant();

        // S3에 이미지 전송
        String s3ImageUrl = storageService.uploadFile(file,"disease", memberId);

        // AI 서버로 전송할 요청 생성
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("image_url", s3ImageUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap<String, String>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        // 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        String fastApiUrl = "http://" + AIEC2ADDRESS +  "/predict"; // "http://<EC2-퍼블릭-IP>/predict"
        ResponseEntity<DiseaseResponse> response = restTemplate.exchange(fastApiUrl, HttpMethod.POST, requestEntity, DiseaseResponse.class);

        // 307 리다이렉트 처리
        if (response.getStatusCode() == HttpStatus.TEMPORARY_REDIRECT) {
            String newUrl = response.getHeaders().getLocation().toString(); // 새로운 URL 가져오기
            response = restTemplate.exchange(newUrl, HttpMethod.POST, requestEntity, DiseaseResponse.class);
        }

        DiseaseResponse result = response.getBody();
        return result;
    }
}

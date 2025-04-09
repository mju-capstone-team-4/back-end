package org.example.mjuteam4.disease;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.disease.dto.DiseaseRequest;
import org.example.mjuteam4.disease.dto.DiseaseResponse;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/disease")
public class DiseaseController {
    private final DiseaseService diseaseService;
    private final JwtUtil jwtUtil;
    @PostMapping("/predict")
    public ResponseEntity<DiseaseResponse> predict(@ModelAttribute DiseaseRequest diseaseRequest) throws IOException {
        System.out.println("here");
        Long memberId = jwtUtil.getLoginMember().getId();
        DiseaseResponse diseaseResponse = diseaseService.predict(diseaseRequest, memberId); // 임시 memberId
        return ResponseEntity.ok(diseaseResponse);
    }
}

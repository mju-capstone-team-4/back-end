package org.example.mjuteam4.disease;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.disease.dto.DiseaseRequest;
import org.example.mjuteam4.disease.dto.DiseaseResponse;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/disease")
public class DiseaseController {
    private final DiseaseService diseaseService;
    private final JwtUtil jwtUtil;
    @PostMapping("/predict")
    public CompletableFuture<ResponseEntity<DiseaseResponse>> predict(@ModelAttribute DiseaseRequest diseaseRequest) throws IOException {
        Long memberId = jwtUtil.getLoginMember().getId();
        log.debug("disease controller thread: " + Thread.currentThread());
        return diseaseService.predict(diseaseRequest,memberId).thenApply(diseaseResponse -> {
            log.debug("disease controller return space thread: " + Thread.currentThread());
            return ResponseEntity.ok(diseaseResponse);
        });

    }
}

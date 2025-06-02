package org.example.mjuteam4.disease;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.disease.dto.ClientDiseaseResponse;
import org.example.mjuteam4.disease.dto.aiServer.AiServerRequest;
import org.example.mjuteam4.disease.entity.Disease;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public CompletableFuture<ResponseEntity<ClientDiseaseResponse>> predict(@ModelAttribute AiServerRequest aiServerRequest) throws IOException {
        Long memberId = jwtUtil.getLoginMember().getId();
        SecurityContext context = SecurityContextHolder.getContext(); // 이 시점에 복사

        log.debug("predict memberId: {}", memberId);
        log.debug("disease controller thread: " + Thread.currentThread());
        return diseaseService.predict(aiServerRequest, memberId)
                .thenApply(diseaseResponse -> {
                    SecurityContextHolder.setContext(context);

                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    log.info("응답 직전 인증 정보: {}", auth);

                    return ResponseEntity.ok(diseaseResponse);
                })
                .exceptionally(ex -> {
                    log.error(" 예외 발생 - 응답 덮어쓰기 방지: {}", ex.getMessage(), ex);
                    // 여기서 직접 500이나 400 등으로 응답
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null); // 또는 커스텀 에러 DTO
                });
    }


    @GetMapping("/record")
    public ResponseEntity<Page<ClientDiseaseResponse>> getRecord(@RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Long memberId = jwtUtil.getLoginMember().getId();
        Pageable pageable = PageRequest.of(page,size);
        Page<Disease> diseaseRecord = diseaseService.getDiseaseRecord(pageable, memberId);
        Page<ClientDiseaseResponse> response = diseaseRecord.map(ClientDiseaseResponse::createWith);
        return ResponseEntity.ok().body(response);
    }
}

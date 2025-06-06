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
    public ResponseEntity<ClientDiseaseResponse> predict(@ModelAttribute AiServerRequest aiServerRequest) throws IOException {
        Long memberId = jwtUtil.getLoginMember().getId();
        ClientDiseaseResponse response = diseaseService.predict(aiServerRequest, memberId);
        return ResponseEntity.ok().body(response);
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

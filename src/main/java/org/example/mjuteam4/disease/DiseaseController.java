package org.example.mjuteam4.disease;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.disease.dto.PredictResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/disease")
public class DiseaseController {
    private final DiseaseService diseaseService;
    @PostMapping("/predict")
    public ResponseEntity<PredictResponse> predict(@RequestParam(value="file")MultipartFile file) throws IOException {
        PredictResponse predictResponse = diseaseService.predict(file, 1L); // 임시 memberId
        return ResponseEntity.ok(predictResponse);

    }
}

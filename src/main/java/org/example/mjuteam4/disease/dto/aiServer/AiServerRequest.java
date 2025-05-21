package org.example.mjuteam4.disease.dto.aiServer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class AiServerRequest {

    @NotBlank(message = "식물 진단 기능의 필수 값 입니다.")
    MultipartFile file;

    @NotBlank(message = "식물 진단 기능의 필수 값 입니다.")
    String description;

    @NotBlank(message = "식물 진단 기능의 필수 값 입니다.")
    String plant;
}

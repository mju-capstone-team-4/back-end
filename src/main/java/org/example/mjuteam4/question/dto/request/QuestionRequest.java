package org.example.mjuteam4.question.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class QuestionRequest {
    private String title;
    private String content;
    private MultipartFile image;
}

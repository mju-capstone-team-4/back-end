package org.example.mjuteam4.question.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

// 질문 등록, 수정에 사용되는 dto
@Data
@NoArgsConstructor
public class QuestionRequest {
    private String title;
    private String content;
    private MultipartFile image;
}

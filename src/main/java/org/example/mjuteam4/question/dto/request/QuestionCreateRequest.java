package org.example.mjuteam4.question.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

// 질문 등록, 수정에 사용되는 dto
@Data
@NoArgsConstructor
public class QuestionCreateRequest {

    @NotBlank(message = "질문 생성의 필수 값 입니다.")
    @Size(max = 40, message = "제목은 40자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "질문 생성의 필수 값 입니다.")
    @Size(max = 500, message = "내용은 500자 이하로 입력해주세요.")
    private String content;

    @NotBlank(message = "질문 생성의 필수 값 입니다.")
    private MultipartFile image;
}

package org.example.mjuteam4.question.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.question.entity.Question;
import org.example.mjuteam4.questionImage.QuestionImageService;
import org.example.mjuteam4.questionImage.entity.QuestionImage;

@Data
@NoArgsConstructor
public class QuestionResponse{
    private Long questionId;
    private String title;
    private String content;
    private String image_url;

    private QuestionResponse(final Question question) {
        this.questionId = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.image_url = question.getQuestionImage().getImageUrl();
    }

    public static QuestionResponse createQuestionResponse(Question question) {
        return new QuestionResponse(question);
    }

}

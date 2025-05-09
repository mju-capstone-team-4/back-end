package org.example.mjuteam4.question.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.question.entity.Question;

@Data
@NoArgsConstructor
public class QuestionResponse{

    //member
    private Long memberId;
    private String username;

    //question
    private Long questionId;
    private String title;
    private String content;
    private String image_url;

    private QuestionResponse(final Question question) {
        Member member = question.getMember();
        this.memberId = member.getId();
        this.username = member.getUsername();

        this.questionId = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.image_url = question.getQuestionImage().getImageUrl();

    }

    public static QuestionResponse createQuestionResponse(Question question) {
        return new QuestionResponse(question);
    }

}

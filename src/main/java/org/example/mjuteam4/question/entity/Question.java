package org.example.mjuteam4.question.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.member.entity.Member;
import org.example.mjuteam4.comment.Comment;
import org.example.mjuteam4.questionImage.entity.QuestionImage;
import org.example.mjuteam4.question.dto.request.QuestionRequest;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Slf4j
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private QuestionImage questionImage;

    // 생성자
    private Question(final QuestionRequest questionRequest) {
        this.title = questionRequest.getTitle();
        this.content = questionRequest.getContent();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 생성 메서드
    public static Question createQuestion(QuestionRequest questionRequest){
        return new Question(questionRequest);
    }

    // 수정 메서드
    public Question modifyQuestion(QuestionRequest questionRequest) {
        String title = questionRequest.getTitle();
        String content = questionRequest.getContent();

        if(StringUtils.hasText(title)){
            this.title = questionRequest.getTitle();
        }
        if(StringUtils.hasText(content)) {
            this.content = questionRequest.getContent();
        }

        this.updatedAt = LocalDateTime.now();
        return this;
    }

    // 연관관계 편의 메서드
    public void setQuestionImage(QuestionImage questionImage){
        this.questionImage = questionImage;
        questionImage.setQuestion(this);
    }
}

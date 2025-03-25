package org.example.mjuteam4.questionImage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.question.entity.Question;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class QuestionImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_image_id")
    private Long id;
    private LocalDateTime uploadedAt;
    private String imageUrl;

    @JoinColumn(name = "question_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Question question;

    // 생성자
    private QuestionImage(String imageUrl) {
        this.uploadedAt = LocalDateTime.now();
        this.imageUrl = imageUrl;
    }

    // 생성 메서드
    public static QuestionImage createQuestionImage(String imageUrl) {
        return new QuestionImage(imageUrl);
    }
}

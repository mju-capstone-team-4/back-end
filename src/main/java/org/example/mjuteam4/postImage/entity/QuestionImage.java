package org.example.mjuteam4.postImage.entity;

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
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;
}

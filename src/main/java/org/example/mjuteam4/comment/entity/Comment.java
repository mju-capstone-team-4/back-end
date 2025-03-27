package org.example.mjuteam4.comment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.comment.dto.request.CommentRequest;
import org.example.mjuteam4.member.entity.Member;
import org.example.mjuteam4.question.entity.Question;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String content;
    private Long recommendCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "question_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    // 생성자
    private Comment(final CommentRequest commentRequest){
        this.content = commentRequest.getComment();
        this.recommendCount = 0L;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    //생성 메서드
    public static Comment createComment(CommentRequest commentRequest) {
        return new Comment(commentRequest);
    }

    //수정 메서드
    public Comment modifyComment(CommentRequest commentRequest) {
        this.content = commentRequest.getComment();
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    //의존관계 설정
    public void setQuestion(Question question){
        this.question = question;
        question.getComments().add(this);
    }

    public void setMember(Member member){
        this.member = member;
        member.getComments().add(this);
    }
}

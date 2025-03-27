package org.example.mjuteam4.comment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.comment.dto.request.CommentRequest;
import org.example.mjuteam4.commentLike.entity.CommentLike;
import org.example.mjuteam4.member.entity.Member;
import org.example.mjuteam4.question.entity.Question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "comment", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CommentLike> commentLikes = new ArrayList<>();

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

    // 댓글 좋아요 수 조회 메서드
    public int countCommentLike(){
        return commentLikes.size();
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

    public void addCommentLike(CommentLike commentLike){
        commentLikes.add(commentLike);
        commentLike.setComment(this);
    }

    public void removeCommentLike(CommentLike commentLike) {
        if(commentLikes.contains(commentLike)) {
            commentLikes.remove(commentLike);
            commentLike.setComment(null);
        }
    }
}

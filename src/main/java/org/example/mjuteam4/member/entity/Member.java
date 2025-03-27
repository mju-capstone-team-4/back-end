package org.example.mjuteam4.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.commentLike.entity.CommentLike;
import org.example.mjuteam4.question.entity.Question;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<Question> questions = new ArrayList<Question>();

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<CommentLike> commentLikes = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addQuestion(Question question) {
        questions.add(question);
        question.setMember(this);
    }

    public void addCommentLike(CommentLike commentLike){
        commentLikes.add(commentLike);
        commentLike.setMember(this);
    }
}

package org.example.mjuteam4.mypage.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.commentLike.entity.CommentLike;
import org.example.mjuteam4.disease.dto.Disease;
import org.example.mjuteam4.question.entity.Question;
import org.example.mjuteam4.tradePost.entity.TradePost;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String username;

    private String profileUrl;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    public enum Role{
        ROLE_USER, ROLE_ADMIN
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyPlant> myPlantList = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<Question> questions = new ArrayList<Question>();

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<TradePost> tradePosts = new ArrayList<>();

    // 식물 진단
    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    public List<Disease> diseaseList = new ArrayList<>();


    public Member(String email, String username, String profileUrl) {
        this.email = email;
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Builder
    public Member(String email, String username, String profileUrl, String refreshToken, Role role, List<MyPlant> myPlantList) {
        this.email = email;
        this.username = username;
        this.profileUrl = profileUrl;
        this.refreshToken = refreshToken;
        this.role = role;
        this.myPlantList = myPlantList;
    }

    public void updateMember(String email, String username){
        this.email = email;
        this.username = username;
    }

    // 연관관계 편의 메서드
    public void addQuestion(Question question) {
        questions.add(question);
        question.setMember(this);
    }

    public void addCommentLike(CommentLike commentLike){
        commentLikes.add(commentLike);
        commentLike.setMember(this);
    }

    public void addTradePost(TradePost tradePost){
        tradePosts.add(tradePost);
        tradePost.setMember(this);
    }
}

package org.example.mjuteam4.commentLike.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.global.domain.LikeBaseEntity;
import org.example.mjuteam4.mypage.entity.Member;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Setter
@Getter
public class CommentLike extends LikeBaseEntity {
    @JoinColumn(name = "comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    private CommentLike(final Member member, final Comment comment) {
        this.member = member;
        this.comment = comment;
        this.likedAt = LocalDateTime.now();
    }

    public static CommentLike createCommentLike(final Member member, final Comment comment){
        return new CommentLike(member,comment);
    }

}

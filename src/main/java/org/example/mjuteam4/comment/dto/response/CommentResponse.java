package org.example.mjuteam4.comment.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.commentLike.entity.CommentLike;
import org.example.mjuteam4.mypage.entity.Member;

import java.util.List;

@Data
@NoArgsConstructor
@Slf4j
public class CommentResponse {

    private Long memberId;
    private String username;
    private String email;

    private Long commentId;
    private String content;
    private Integer likeCount;
    private boolean liked;

    private CommentResponse(final Comment comment, Long authUserId) {
        Member member = comment.getMember();
        memberId = member.getId(); // 댓글 작성자 id
        username = member.getUsername(); // 댓글 작성자 이름
        email = member.getEmail(); // 댓글 작성자 이메일

        List<CommentLike> commentLikes = comment.getCommentLikes();

        for (CommentLike commentLike : commentLikes) {
            if(commentLike.getMember().getId().equals(authUserId)){
                this.liked = true;
                break;
            }
        }

        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.likeCount = comment.getCommentLikes().size();
    }

    private CommentResponse(final Comment comment) {
        Member member = comment.getMember();
        memberId = member.getId(); // 댓글 작성자 id
        username = member.getUsername(); // 댓글 작성자 이름
        email = member.getEmail(); // 댓글 작성자 이메일

        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.likeCount = comment.getCommentLikes().size();
        this.liked = true;
    }

    public static CommentResponse createWithAuthUserId(final Comment comment, final Long authUserId) {

        return new CommentResponse(comment, authUserId);
    }

    public static CommentResponse create(final Comment comment) {
        return new CommentResponse(comment);
    }

}

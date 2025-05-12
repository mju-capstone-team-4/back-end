package org.example.mjuteam4.comment.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.mypage.entity.Member;

@Data
@NoArgsConstructor
public class CommentResponse {

    private Long memberId;
    private String username;


    private Long commentId;
    private String content;
    private Integer likeCount;
    private boolean liked;

    private CommentResponse(final Comment comment) {
        Member member = comment.getMember();
        memberId = member.getId();
        username = member.getUsername();
        
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.likeCount = comment.getCommentLikes().size();
        this.liked = this.likeCount > 0;
    }

    public static CommentResponse createCommentResponse(final Comment comment) {
        return new CommentResponse(comment);
    }

}

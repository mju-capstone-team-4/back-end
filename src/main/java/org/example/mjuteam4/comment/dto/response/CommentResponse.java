package org.example.mjuteam4.comment.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.comment.entity.Comment;

@Data
@NoArgsConstructor
public class CommentResponse {
    private Long commentId;
    private String content;
    private Integer likeCount;

    private CommentResponse(final Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.likeCount = comment.getCommentLikes().size();
    }

    public static CommentResponse createCommentResponse(final Comment comment) {
        return new CommentResponse(comment);
    }

}

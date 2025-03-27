package org.example.mjuteam4.commentLike.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.commentLike.entity.CommentLike;

@Data
@NoArgsConstructor
public class CommentLikeResponse {
    private Long commentId;
    private Integer likeCount;
    private boolean liked;

    private CommentLikeResponse(final CommentLike commentLike, final boolean liked) {
        Comment comment = commentLike.getComment();
        this.commentId = comment.getId();
        this.likeCount = comment.countCommentLike();
        this.liked = liked;
    }

    private CommentLikeResponse(final Comment comment, final boolean liked) {
        this.commentId = comment.getId();
        this.likeCount = comment.countCommentLike();
        this.liked = liked;
    }

    public static CommentLikeResponse createWithCommentLike(final CommentLike commentLike, final boolean liked){
        return new CommentLikeResponse(commentLike, liked);
    }

    public static CommentLikeResponse createWithComment(final Comment comment, final boolean liked) {
        return new CommentLikeResponse(comment, liked);
    }

}

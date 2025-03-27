package org.example.mjuteam4.commentLike;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.comment.CommentService;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.commentLike.dto.response.CommentLikeResponse;
import org.example.mjuteam4.commentLike.entity.CommentLike;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/")
public class CommentLikeController {

    private final Long memberId = 999L;
    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    // 좋아요 완료
    @PostMapping("/{commentId}/likes")
    public ResponseEntity<CommentLikeResponse> likeUp(
            @PathVariable(value = "commentId") Long commentId){
        CommentLike commentLike = commentLikeService.increaseLike(memberId, commentId);
        CommentLikeResponse commentLikeResponse = CommentLikeResponse.createWithCommentLike(commentLike, true);
        return ResponseEntity.status(201).body(commentLikeResponse);
    }

    // 좋아요 취소
    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<CommentLikeResponse> likeDown(
            @PathVariable(value = "commentId") Long commentId)
    {
        commentLikeService.decreaseLike(memberId,commentId);
        Comment comment = commentService.findByCommentId(commentId);
        CommentLikeResponse commentLikeResponse = CommentLikeResponse.createWithComment(comment, false);
        return ResponseEntity.ok().body(commentLikeResponse);
    }

    // 총 좋아요 수
    @GetMapping("/{commentId}/likes/count")
    public ResponseEntity<Integer> likeCount(
            @PathVariable(value = "commentId") Long commentId)
    {
        Integer totalLike = commentLikeService.getTotalLike(commentId);
        return ResponseEntity.ok().body(totalLike);
    }

}

package org.example.mjuteam4.commentLike;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.comment.CommentService;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.commentLike.dto.response.CommentLikeResponse;
import org.example.mjuteam4.commentLike.dto.response.CommentLikeTotalResponse;
import org.example.mjuteam4.commentLike.entity.CommentLike;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments/")
public class CommentLikeController {
    private final JwtUtil jwtUtil;
    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    // 좋아요 완료
    @PostMapping("/{comment-id}/likes")
    public ResponseEntity<CommentLikeResponse> likeUp(
            @PathVariable(value = "comment-id") Long commentId){
        Long memberId = jwtUtil.getLoginMember().getId();
        CommentLike commentLike = commentLikeService.increaseLike(memberId, commentId);
        CommentLikeResponse commentLikeResponse = CommentLikeResponse.createWithCommentLike(commentLike, true);
        return ResponseEntity.status(201).body(commentLikeResponse);
    }

    // 좋아요 취소
    @DeleteMapping("/{comment-id}/likes")
    public ResponseEntity<CommentLikeResponse> likeDown(
            @PathVariable(value = "comment-id") Long commentId)
    {
        Long memberId = jwtUtil.getLoginMember().getId();
        commentLikeService.decreaseLike(memberId,commentId);
        Comment comment = commentService.findByCommentId(commentId);
        CommentLikeResponse commentLikeResponse = CommentLikeResponse.createWithComment(comment, false);
        return ResponseEntity.ok().body(commentLikeResponse);
    }

    // 총 좋아요 수
    @GetMapping("/{comment-id}/likes/count")
    public ResponseEntity<CommentLikeTotalResponse> likeCount(
            @PathVariable(value = "comment-id") Long commentId)
    {
        Integer totalLike = commentLikeService.getTotalLike(commentId);
        CommentLikeTotalResponse response = CommentLikeTotalResponse.builder().totalLikeCount(totalLike).build();
        return ResponseEntity.ok().body(response);
    }

}

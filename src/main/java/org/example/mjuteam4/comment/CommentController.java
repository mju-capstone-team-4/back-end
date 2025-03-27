package org.example.mjuteam4.comment;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.comment.dto.request.CommentRequest;
import org.example.mjuteam4.comment.dto.response.CommentResponse;
import org.example.mjuteam4.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final Long memberId = 9999L; // 임시용 memberId
    private final CommentService commentService;
    @PostMapping("/api/question/{question_id}/comment")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable(value = "question_id") Long questionId,
            @RequestBody CommentRequest commentRequest
    ) {
        Comment comment = commentService.commentCreate(memberId, questionId, commentRequest);
        CommentResponse commentResponse = CommentResponse.createCommentResponse(comment);
        return ResponseEntity.ok().body(commentResponse);
    }

    @GetMapping("/api/question/{question_id}/comments")
    public Page<CommentResponse> getCommentList(
            @PathVariable(value = "question_id") Long questionId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentService.getCommentList(pageable);
        return comments.map(CommentResponse::createCommentResponse);
    }

    @PutMapping("/api/comment/{comment_id}")
    public ResponseEntity<CommentResponse> modifyComment(
            @PathVariable(value = "comment_id") Long commentId,
            @RequestBody CommentRequest commentRequest)
    {
        Comment comment = commentService.commentModify(memberId, commentId, commentRequest);
        CommentResponse commentResponse = CommentResponse.createCommentResponse(comment);
        return ResponseEntity.ok().body(commentResponse);
    }

    @DeleteMapping("/api/comment/{comment_id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "comment_id") Long commentId){
        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.ok().body("comment deleted");
    }

    // 추천 기능


}

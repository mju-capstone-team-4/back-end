package org.example.mjuteam4.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.comment.dto.request.CommentRequest;
import org.example.mjuteam4.comment.dto.response.CommentResponse;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final JwtUtil jwtUtil;
    private final CommentService commentService;
    @PostMapping("/question/{question_id}/comment")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable(value = "question_id") Long questionId,
            @RequestBody CommentRequest commentRequest
    ) {
        log.debug("create comment: {}", commentRequest.getComment());
        Long memberId = jwtUtil.getLoginMember().getId();
        Comment comment = commentService.commentCreate(memberId, questionId, commentRequest);
        CommentResponse commentResponse = CommentResponse.create(comment);
        return ResponseEntity.ok().body(commentResponse);
    }

    @GetMapping("/question/{question_id}/comments")
    public Page<CommentResponse> getCommentList(
            @PathVariable(value = "question_id") Long questionId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Long memberId = jwtUtil.getLoginMember().getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentService.getCommentList(questionId, pageable);
        return comments.map(c -> CommentResponse.createWithAuthUserId(c, memberId));
    }

    @PutMapping("/comment/{comment_id}")
    public ResponseEntity<CommentResponse> modifyComment(
            @PathVariable(value = "comment_id") Long commentId,
            @RequestBody CommentRequest commentRequest)
    {
        Long memberId = jwtUtil.getLoginMember().getId();
        Comment comment = commentService.commentModify(memberId, commentId, commentRequest);
        CommentResponse commentResponse = CommentResponse.create(comment);
        return ResponseEntity.ok().body(commentResponse);
    }

    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "comment_id") Long commentId){
        Long memberId = jwtUtil.getLoginMember().getId();
        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.ok().body("comment deleted");
    }

    // 추천 기능


}

package org.example.mjuteam4.comment;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.comment.dto.request.CommentRequest;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.comment.exception.CommentNotFound;
import org.example.mjuteam4.comment.exception.CommentAccessUnauthorized;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.question.QuestionService;
import org.example.mjuteam4.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final QuestionService questionService;
    private final JwtUtil jwtUtil;
    public Comment commentCreate(Long memberId, Long questionId, CommentRequest commentRequest) {
        Question question = questionService.findQuestionById(questionId);
        Member member = jwtUtil.getLoginMember();
        Comment comment = Comment.createComment(commentRequest);
        comment.setQuestion(question);
        comment.setMember(member);
        commentRepository.save(comment);
        return comment;
    }

    public Page<Comment> getCommentList(Pageable pageable)
    {
        return commentRepository.findAll(pageable);
    }

    public Comment commentModify(Long memberId, Long commentId, CommentRequest commentRequest){
        // 1. 접근 권한을 체크한다.
        Comment comment = checkAuthorizedAccess(memberId, commentId);

        // 2. 댓글을 수정한다.
        return comment.modifyComment(commentRequest);
    }

    public void deleteComment(Long memberId, Long commentId){
        // 1. 접근 권한을 체크한다.
        Comment comment = checkAuthorizedAccess(memberId, commentId);

        // 2. 댓글을 수정한다.
        commentRepository.deleteById(commentId);

    }

    // 댓글 수정 및 삭제 권한 확인
    public Comment checkAuthorizedAccess(Long memberId, Long commentId) {
        // 1. comment를 조회한다.
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);

        // 2. 현재 로그인한 사용자가 댓글 수정 권한이 있는 지 체크한다.
        if(!comment.getMember().getId().equals(memberId)) {
            throw new CommentAccessUnauthorized();
        }

        return comment;
    }

    // 댓글 id로 댓글 조회하기
    public Comment findByCommentId(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);
    }
}

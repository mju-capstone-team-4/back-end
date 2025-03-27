package org.example.mjuteam4.commentLike;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.comment.CommentService;
import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.commentLike.entity.CommentLike;
import org.example.mjuteam4.commentLike.exception.AlreadyLikedException;
import org.example.mjuteam4.member.MemberService;
import org.example.mjuteam4.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final MemberService memberService;
    private final CommentService commentService;

    public CommentLike increaseLike(Long memberId, Long commentId) {
        Member member = memberService.findByMemberId(memberId);
        Comment comment = commentService.findByCommentId(commentId);


        if(commentLikeRepository.existsByMemberAndComment(member, comment)) {
            throw new AlreadyLikedException();
        }

        CommentLike commentLike = CommentLike.createCommentLike(member, comment);
        member.addCommentLike(commentLike);
        comment.addCommentLike(commentLike);
        return commentLikeRepository.save(commentLike);
    }


    public void decreaseLike(Long memberId, Long commentId){
        Member member = memberService.findByMemberId(memberId);
        Comment comment = commentService.findByCommentId(commentId);

        if(!commentLikeRepository.existsByMemberAndComment(member, comment)) {
            throw new RuntimeException("좋아요 하지 않은 상태에서 좋아요를 취소할 수는 없음");
        }

        CommentLike commentLike = commentLikeRepository.findByMemberAndComment(member, comment);
        comment.removeCommentLike(commentLike);
        commentLikeRepository.delete(commentLike);
    }

    public Integer getTotalLike(Long commentId){
        return commentService.findByCommentId(commentId).countCommentLike();
    }
}

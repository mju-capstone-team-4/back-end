package org.example.mjuteam4.commentLike;


import org.example.mjuteam4.comment.entity.Comment;
import org.example.mjuteam4.commentLike.entity.CommentLike;
import org.example.mjuteam4.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByMemberAndComment(Member member, Comment comment);

    CommentLike findByMemberAndComment(Member member, Comment comment);

}

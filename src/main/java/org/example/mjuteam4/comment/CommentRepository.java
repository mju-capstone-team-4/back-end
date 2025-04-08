package org.example.mjuteam4.comment;

import org.example.mjuteam4.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByQuestionId(Long questionId, Pageable pageable);
}

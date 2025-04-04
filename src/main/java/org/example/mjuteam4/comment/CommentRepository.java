package org.example.mjuteam4.comment;

import org.example.mjuteam4.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}

package org.example.mjuteam4.question;

import org.example.mjuteam4.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByMemberId(Long memberId, Pageable pageable);

}

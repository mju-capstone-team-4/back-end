package org.example.mjuteam4.question;

import org.example.mjuteam4.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}

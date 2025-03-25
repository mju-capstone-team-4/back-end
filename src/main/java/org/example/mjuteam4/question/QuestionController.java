package org.example.mjuteam4.question;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.question.dto.request.QuestionRequest;
import org.example.mjuteam4.question.dto.response.QuestionResponse;
import org.example.mjuteam4.question.entity.Question;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> questionCreate(@ModelAttribute QuestionRequest questionRequest) {
        // 임시 memberId, 로컬에서 테스트 진행할 때, 직접 DB에 memberId가 999인 member를 생성
        // 후에 회원 관련 기능 개발 후 수정
        Long memberId = 999L;
        Question question = questionService.createQuestion(memberId, questionRequest);
        QuestionResponse questionResponse = QuestionResponse.createQuestionResponse(question);
        return ResponseEntity.ok().body(questionResponse);
    }

    /*
    @GetMapping("/all")
    public ResponseEntity<List<QuestionResponse>> questionList() {

    }

    @GetMapping("/{question_id}")
    public ResponseEntity<QuestionResponse> questionDetail() {
        questionService.questionDetail();;
    }

    @PutMapping("/{question_id}")
    public ResponseEntity<QuestionResponse> questionModify() {
        questionService.modifyQuestion();
    }

    @DeleteMapping("/{question_id}")
    public ResponseEntity<QuestionResponse> questionDelete() {
        questionService.deleteQuestion();
    }
     */
}

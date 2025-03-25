package org.example.mjuteam4.question;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.question.dto.request.QuestionRequest;
import org.example.mjuteam4.question.dto.response.QuestionResponse;
import org.example.mjuteam4.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all")
    public ResponseEntity<Page<QuestionResponse>> questionList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionResponse> response = questionService.questionList(pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{question_id}")
    public ResponseEntity<QuestionResponse> questionDetail(@PathVariable(value = "question_id") Long questionId) {
        Question question = questionService.questionDetail(questionId);
        QuestionResponse questionResponse = QuestionResponse.createQuestionResponse(question);
        return ResponseEntity.ok().body(questionResponse);
    }

    /*
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

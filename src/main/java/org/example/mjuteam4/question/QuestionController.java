package org.example.mjuteam4.question;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.question.dto.request.QuestionCreateRequest;
import org.example.mjuteam4.question.dto.request.QuestionUpdateRequest;
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

    private final JwtUtil jwtUtil;
    private final QuestionService questionService;
    // 내 질문 게시글 모아보기
    @GetMapping("/my")
    public ResponseEntity<Page<QuestionResponse>> getMyQuestion(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Long id = jwtUtil.getLoginMember().getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions = questionService.getMyQuestion(id,pageable);
        Page<QuestionResponse> response = questions.map(QuestionResponse::createQuestionResponse);
        return ResponseEntity.ok().body(response);
    }

    // 질문 생성
    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> questionCreate(@ModelAttribute QuestionCreateRequest questionCreateRequest) {
        Long memberId = jwtUtil.getLoginMember().getId();
        Question question = questionService.createQuestion(memberId, questionCreateRequest);
        QuestionResponse questionResponse = QuestionResponse.createQuestionResponse(question);
        return ResponseEntity.ok().body(questionResponse);
    }

    // 질문 여러 건 조회(기본 10개)
    @GetMapping("/all")
    public ResponseEntity<Page<QuestionResponse>> questionList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionResponse> response = questionService.questionList(pageable);
        return ResponseEntity.ok().body(response);
    }

    // 질문 단 건 조회
    @GetMapping("/{question_id}")
    public ResponseEntity<QuestionResponse> questionDetail(@PathVariable(value = "question_id") Long questionId) {
        Question question = questionService.questionDetail(questionId);
        QuestionResponse questionResponse = QuestionResponse.createQuestionResponse(question);
        return ResponseEntity.ok().body(questionResponse);
    }

    // 질문 수정
    @PutMapping("/{question_id}")
    public ResponseEntity<QuestionResponse> questionModify(
            @PathVariable(value = "question_id") Long questionId,
            @ModelAttribute QuestionUpdateRequest questionUpdateRequest
    ) {
        Long memberId = jwtUtil.getLoginMember().getId();
        Question question = questionService.modifyQuestion(memberId, questionId, questionUpdateRequest);
        QuestionResponse questionResponse = QuestionResponse.createQuestionResponse(question);
        return ResponseEntity.ok().body(questionResponse);
    }

    //질문 삭제
    @DeleteMapping("/{question_id}")
    public ResponseEntity<String> questionDelete(
            @PathVariable(value = "question_id") Long questionId)
    {
        Long memberId = jwtUtil.getLoginMember().getId();
        questionService.deleteQuestion(memberId, questionId);
        return ResponseEntity.ok().body("Deleted Success");
    }

}

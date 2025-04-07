package org.example.mjuteam4.question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.question.dto.request.QuestionCreateRequest;
import org.example.mjuteam4.question.dto.request.QuestionUpdateRequest;
import org.example.mjuteam4.question.dto.response.QuestionResponse;
import org.example.mjuteam4.question.entity.Question;
import org.example.mjuteam4.question.exception.QuestionNotFound;
import org.example.mjuteam4.questionImage.QuestionImageService;
import org.example.mjuteam4.questionImage.entity.QuestionImage;
import org.example.mjuteam4.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuestionService {

    private final QuestionImageService questionImageService;
    private final StorageService storageService;
    private final JwtUtil jwtUtil;

    private final QuestionRepository questionRepository;

    public Question createQuestion(Long memberId, QuestionCreateRequest questionCreateRequest) {
        Question question = Question.createQuestion(questionCreateRequest);
        QuestionImage questionImage = questionImageService.createQuestionImage(questionCreateRequest.getImage());
        question.setQuestionImage(questionImage);
        Member member = jwtUtil.getLoginMember();
        member.addQuestion(question);
        return questionRepository.save(question);
    }

    public Page<QuestionResponse> questionList(Pageable pageable) {
        Page<Question> questions = questionRepository.findAll(pageable);
        return questions.map(QuestionResponse::createQuestionResponse);
    }

    public Question questionDetail(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);
    }

    public Question modifyQuestion(Long questionId, QuestionUpdateRequest questionUpdateRequest) {

        // 수정 대상 질문 조회 후 제목, 내용 수정
        Question modifiedQuestion = questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new).modifyQuestion(questionUpdateRequest);

        // 이미지 수정
        // 이미지 변경 요청이 있다면, 기존 s3이미지 삭제하고 새로운 이미지 업로드
        if(questionUpdateRequest.getImage() != null){

            String originalImageUrl = modifiedQuestion.getQuestionImage().getImageUrl();
            questionImageService.deleteS3Image(originalImageUrl); //s3 이미지 삭제
            modifiedQuestion.setQuestionImage(null); // 고아 객체 제거 유도
            questionRepository.flush();

            MultipartFile image = questionUpdateRequest.getImage();
            QuestionImage questionImage = questionImageService.createQuestionImage(image);

            modifiedQuestion.setQuestionImage(questionImage);
        }
        return modifiedQuestion;
    }

    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

    public Question findQuestionById(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(QuestionNotFound::new);
    }



}

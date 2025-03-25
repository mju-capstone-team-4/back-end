package org.example.mjuteam4.question;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.member.MemberRepository;
import org.example.mjuteam4.member.MemberService;
import org.example.mjuteam4.member.entity.Member;
import org.example.mjuteam4.question.dto.request.QuestionRequest;
import org.example.mjuteam4.question.entity.Question;
import org.example.mjuteam4.questionImage.QuestionImageService;
import org.example.mjuteam4.questionImage.entity.QuestionImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final MemberService memberService;
    private final QuestionImageService questionImageService;
    private final QuestionRepository questionRepository;

    public Question createQuestion(Long memberId, QuestionRequest questionRequest) {
        Question question = Question.createQuestion(questionRequest);
        QuestionImage questionImage = questionImageService.createQuestionImage(questionRequest.getImage());
        question.addImage(questionImage);
        Member member = memberService.findById(memberId);
        member.addQuestion(question);
        return questionRepository.save(question);
    }

    public void questionDetail() {

    }

    public void modifyQuestion() {

    }

    public void deleteQuestion() {

    }



}

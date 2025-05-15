package org.example.mjuteam4.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.chatbot.dto.ChatBotRequestDto;
import org.example.mjuteam4.chatbot.dto.ChatBotResponseDto;
import org.example.mjuteam4.disease.dto.PrescriptionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CompletableFuture<ChatBotResponseDto> chatJson(ChatBotRequestDto chatBotRequestDto) {
        return CompletableFuture.supplyAsync(() -> {
            return chatClient.prompt()
                    .user(chatBotRequestDto.getMessage())
                    .options(OpenAiChatOptions.builder()
                            .model("gpt-4o")
                            .temperature(0.3)
                            .maxTokens(200)
                            .build())
                    .call()
                    .entity(ChatBotResponseDto.class);
        });
    }

    public PrescriptionResponse generatePrescription(String result) {
        log.info("result: " +result);

        String myPrompt = "질병명: " +
                result  +
                " 질병명에 대한 JSON 객체를 생성해 주세요. 키는 다음과 같아야 합니다: " +
                "- diseaseInfo  - watering  - environment  - nutrition " +
                "watering은 발생한 질병에 대처위한 급수(물주기) 방법" +
                "environment은 발생한 질병에 대처하기 위한 환경 조성을" +
                "nutrition은 발생한 질병을 예방하거나 치료하기 위한 영양 정보를" +
                "diseaseInfo는 발생한 질병의 특징 정보를  포함해야 합니다" +
                "반드시 **마크다운 없이**, **앞뒤에 ``` 같은 기호 없이** 순수 JSON만 출력해 주세요.";
        String response = chatClient.prompt().user(myPrompt)
                .options(OpenAiChatOptions.builder()
                        .model("gpt-4o")
                        .temperature(0.3)
                        .maxTokens(500)
                        .build())
                .call()
                .content();

        log.debug("AI response: {}", response);

        try{
            PrescriptionResponse prescriptionResponse = objectMapper.readValue(response, PrescriptionResponse.class);
            return prescriptionResponse;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}

package org.example.mjuteam4.chatbot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ChatBotConfig {
    @Value("classpath:/prompt.txt")
    private Resource resource;
    @Bean
    public ChatClient chatBotClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder
                .defaultSystem(resource) // LLM 역할 부여
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())) // 메모리 기반의 단기 대화 기록 저장소
                .build();
    }
}

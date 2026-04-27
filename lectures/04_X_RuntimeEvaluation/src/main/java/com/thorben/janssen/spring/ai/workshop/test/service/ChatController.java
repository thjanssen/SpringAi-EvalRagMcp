package com.thorben.janssen.spring.ai.workshop.test.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatController {

    private static final String SYSTEM_PROMPT = """
        You are a friendly and helpful senior Java developer.
        """;

    private final ChatClient chatClient;

    public ChatController(
            @Qualifier("openAiChatClientBuilder") ChatClient.Builder chatClientBuilder,
            @Qualifier("qwen3ChatClientBuilder") ChatClient.Builder evalChatClientBuilder
    ) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                    SimpleLoggerAdvisor.builder().build(),
                    new RuntimeEvaluationAdvisor(evalChatClientBuilder)
                )
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    public Flux<String> chat(String message) {
        return Flux.just(chatClient.prompt(message).call().content());
    }
}

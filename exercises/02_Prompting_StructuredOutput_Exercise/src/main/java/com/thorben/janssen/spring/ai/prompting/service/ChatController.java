package com.thorben.janssen.spring.ai.prompting.service;

import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@Service
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder,
                          @Value("classpath:/prompts/system.st")
                          Resource systemPrompt) {
        chatClient = chatClientBuilder
                .defaultSystem(systemPrompt)
                .build();
    }

    public Flux<String> chat(String message) {
        var response = chatClient.prompt(message)
                .advisors(
                        SimpleLoggerAdvisor.builder().order(Integer.MAX_VALUE).build()
                )
                .call()
                .content();

        return Flux.just(response);
    }

}

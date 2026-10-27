package com.thorben.janssen.spring.ai.memory.jdbc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.session.SessionService;
import org.springframework.ai.session.advisor.SessionAdvisors;
import org.springframework.ai.session.compaction.ChatModelSummarizer;
import org.springframework.ai.session.compaction.TurnCountTrigger;
import org.springframework.ai.session.compaction.WindowCompactionStrategy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Service
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder, ChatModel chatModel, SessionService sessionService) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(SessionAdvisors.builder(sessionService)
                        // once the conversation has 3 turns, keep the last 2 turns
                        // and let the LLM summarize the older ones
                        .compaction(new TurnCountTrigger(3), WindowCompactionStrategy.builder()
                                .lastTurns(2)
                                .summarizer(ChatModelSummarizer.builder(chatModel).build())
                                .build())
                        .build())
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build())
                .build();
    }

    public Flux<String> chat(String message, UUID conversationId) {
        return chatClient.prompt(message)
                .advisors(SessionAdvisors.session(conversationId.toString()))
                .stream()
                .content();
    }
}

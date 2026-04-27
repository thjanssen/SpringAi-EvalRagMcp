package com.thorben.janssen.spring.ai.workshop.mcpclient.rest;

import org.springaicommunity.tool.search.ToolSearchToolCallAdvisor;
import org.springaicommunity.tool.search.ToolSearcher;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private static final String SYSTEM_PROMPT = """
        You are a friendly and helpful senior Java developer.
        """;

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider, ToolSearcher toolSearcher) {
        var toolSearchAdvisor = ToolSearchToolCallAdvisor.builder()
                .toolSearcher(toolSearcher)
                .build();
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
//                    toolSearchAdvisor,
                    new MyLoggingAdvisor(),
                    SimpleLoggerAdvisor.builder().build()
                )
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody String message) {
        return Flux.just(chatClient.prompt(message).call().content());
    }
}

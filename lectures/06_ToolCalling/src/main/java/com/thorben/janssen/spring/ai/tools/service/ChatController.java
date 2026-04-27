package com.thorben.janssen.spring.ai.tools.service;

import com.thorben.janssen.spring.ai.tools.tool.CurrentTimeTool;
import com.thorben.janssen.spring.ai.tools.tool.DummyTools;
import org.springaicommunity.tool.search.ToolSearchToolCallAdvisor;
import org.springaicommunity.tool.search.ToolSearcher;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatController {

//    private static final String SYSTEM_PROMPT = """
//        You are a friendly and helpful senior Java developer.
//        You format all your answers as a HTML snippet so that I looks nice as the content of a <div> on a website using TailwindCSS.
//        """;

    private static final String SYSTEM_PROMPT = """
        You are a friendly and helpful senior Java developer.
        """;

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder, ToolCallingManager toolCallingManager, ToolSearcher toolSearcher) {
        this.chatClient = chatClientBuilder
                .defaultTools(new CurrentTimeTool())
                .defaultAdvisors(
                    ToolCallAdvisor.builder().toolCallingManager(toolCallingManager).build(),
                    ToolSearchToolCallAdvisor.builder().toolSearcher(toolSearcher).build(),
                    new MyLoggingAdvisor(),
                    SimpleLoggerAdvisor.builder().build()
                )
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    public Flux<String> chat(String message) {
        return Flux.just(chatClient.prompt(message).call().content());
    }
}

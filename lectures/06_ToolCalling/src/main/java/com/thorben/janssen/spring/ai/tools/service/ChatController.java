package com.thorben.janssen.spring.ai.tools.service;

import com.thorben.janssen.spring.ai.tools.tool.CurrentTimeTool;
import com.thorben.janssen.spring.ai.tools.tool.DummyTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.MemoryAdvisor;
import org.springframework.ai.chat.client.advisor.toolsearch.ToolSearchToolCallingAdvisor;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
import org.springframework.ai.tool.toolsearch.ToolIndex;
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

    public ChatController(ChatClient.Builder chatClientBuilder, ToolCallingManager toolCallingManager, ToolIndex toolIndex) {
        this.chatClient = chatClientBuilder
                .defaultTools(new CurrentTimeTool())
                .defaultAdvisors(
                    ToolSearchToolCallingAdvisor.builder().toolCallingManager(toolCallingManager).toolIndex(toolIndex).build(),
                    new MyLoggingAdvisor(),
                    SimpleLoggerAdvisor.builder().build()
                )
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    public Flux<String> chat(String message) {
        return Flux.just(chatClient.prompt(message)
                // ToolSearchToolCallingAdvisor scopes its tool index per session
                .advisors(a -> a.param(MemoryAdvisor.CONVERSATION_ID, "default"))
                .call()
                .content());
    }
}

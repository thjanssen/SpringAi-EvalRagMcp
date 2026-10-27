package com.thorben.janssen.spring.ai.tools.service;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.util.JsonHelper;

import java.util.stream.Collectors;

public class MyLoggingAdvisor implements BaseAdvisor {

    private static final JsonHelper JSON_HELPER = new JsonHelper();

    private final int order;

    public MyLoggingAdvisor() {
        this(0);
    }

    public MyLoggingAdvisor(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        Object tools = "No Tools";
        if (chatClientRequest.prompt().getOptions() instanceof ToolCallingChatOptions toolOptions
                && toolOptions.getToolCallbacks() != null) {
            tools = toolOptions.getToolCallbacks().stream().map(tc -> tc.getToolDefinition().name()).toList();
        }

        String mt = chatClientRequest.prompt()
                .getInstructions()
                .stream()
                .map(message -> message.getMessageType() == MessageType.SYSTEM ? " - SYSTEM "
                        : " - " + JSON_HELPER.toJson(message))
                .collect(Collectors.joining("\n"));

        System.out.println("\nUSER:\n" + mt + "\n   TOOLS: " + JSON_HELPER.toJson(tools) + "\n");

        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        String gt = chatClientResponse.chatResponse()
                .getResults()
                .stream()
                .map(g -> " - " + JSON_HELPER.toJson(g.getOutput()))
                .collect(Collectors.joining("\n"));

        System.out.println("\nASSISTANT:\n" + gt + "\n");

        return chatClientResponse;
    }

}
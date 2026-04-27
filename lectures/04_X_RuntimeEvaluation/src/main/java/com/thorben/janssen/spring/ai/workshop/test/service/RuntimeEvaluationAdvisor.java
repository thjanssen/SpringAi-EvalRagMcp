package com.thorben.janssen.spring.ai.workshop.test.service;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;

import java.util.Map;

public class RuntimeEvaluationAdvisor implements CallAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeEvaluationAdvisor.class);

    private final int maxAttempts = 3;

    private ChatClient.Builder chatClientBuilder;

    public RuntimeEvaluationAdvisor(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        var request = chatClientRequest;
        ChatClientResponse response;

        // Retry until maxAttempts reached
        for (int attempt = 1; attempt <= maxAttempts + 1; attempt++) {

            // call LLM
            response = callAdvisorChain.copy(this).nextCall(request);

            // Perform evaluation
            var evaluator = RelevancyEvaluator.builder().chatClientBuilder(chatClientBuilder).build();
            var evalRequest = new EvaluationRequest(request.prompt().getUserMessage().getText(), response.chatResponse().getResult().getOutput().getText());
            var evalResponse = evaluator.evaluate(evalRequest);

            if (evalResponse.isPass()
                    && attempt != 1
            ) {
                return response;
            }

            // If this is the last attempt, return the response regardless
            if (attempt > maxAttempts) {
                return response;
            }

            // Retry with evaluation feedback
            logger.warn("Evaluation failed on attempt {}", attempt);
            Prompt newPrompt = request.prompt()
                    .augmentUserMessage(userMessage -> userMessage.mutate().text(String.format("""
                %s
                The previous response was not relevant. Please try again.
                """, userMessage.getText())).build());

            request = request.mutate().prompt(newPrompt).build();
        }

        // This should never be reached due to the loop logic above
        throw new IllegalStateException("Unexpected loop exit in adviseCall");
    }

    @Override
    public String getName() {
        return "RuntimeEvaluationAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
package com.thorben.janssen.spring.ai.workshop.test;

import com.thorben.janssen.spring.ai.workshop.test.service.ChatController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.ollama.management.PullModelStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.stream.Collectors;

@SpringBootTest
class SpringAiTests {

	private static final Logger logger = LoggerFactory.getLogger(SpringAiTests.class);

	@Autowired
    private ChatController chatController;

	@Test
	void testRelevancy() {
        var question = "Wer ist seit 2000 Fussball Weltmeister geworden?";
        var response = chatController.chat(question).collect(Collectors.joining()).block();
        logger.info("LLM Response: "+response);

//        var evalResponse;
//
//        logger.info(evalResponse.toString());
//        Assertions.assertTrue(evalResponse.isPass());
	}

    @Test
    void testFacts() {
        var question = "Who won the soccer world championship in 2002. Name only the team.";
        var response = chatController.chat(question).collect(Collectors.joining()).block();
        logger.info("LLM Response: "+response);

//        var evalResponse;
//
//        logger.info(evalResponse.toString());
//        Assertions.assertTrue(evalResponse.isPass());
    }

}

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
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.ollama.management.PullModelStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class SpringAiTests {

	private static final Logger logger = LoggerFactory.getLogger(SpringAiTests.class);

	@Autowired
    private ChatController chatController;

	@Test
	void testRelevancy() {
        var question = "Which countries have won the men's FIFA World Cup since 2000?";
        var response = chatController.chat(question).collect(Collectors.joining()).block();
        logger.info("LLM Response: "+response);

        OllamaApi ollamaApi = OllamaApi.builder().build();
        ChatModel chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .modelManagementOptions(ModelManagementOptions.builder()
                        .pullModelStrategy(PullModelStrategy.WHEN_MISSING)
                        .build())
                .options(OllamaChatOptions.builder()
                        .model("qwen3:8b")
                        .temperature(0.0d)
                        .topP(1d)
                        .build())
                .build();
        var chatClientBuilder = ChatClient.builder(chatModel)
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build());

        // the default prompt ends with "Answer:", which qwen3 echoes as "Answer: YES"
        // but the evaluator only accepts a plain "YES"
        var promptTemplate = new PromptTemplate("""
                Your task is to evaluate if the response for the query
				is in line with the context information provided.

				You have two options to answer. Either YES or NO.

                Reply with exactly one word: YES, if the response for the query
				is in line with context information otherwise NO.
                Do not add any other text.

				Query:
				{query}

				Response:
				{response}

				Context:
				{context}
                """);
        var evaluator = RelevancyEvaluator.builder()
                .chatClientBuilder(chatClientBuilder)
                .promptTemplate(promptTemplate)
                .build();
        var evalRequest = new EvaluationRequest(question, response);
        var evalResponse = evaluator.evaluate(evalRequest);

        logger.info(evalResponse.toString());
        Assertions.assertTrue(evalResponse.isPass());
	}

    @Test
    void testFacts() {
        var question = "Who won the soccer world championship in 2002. Name only the team.";
        var response = chatController.chat(question).collect(Collectors.joining()).block();
        logger.info("LLM Response: "+response);

        OllamaApi ollamaApi = OllamaApi.builder().build();
        ChatModel chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .modelManagementOptions(ModelManagementOptions.builder()
                                        .pullModelStrategy(PullModelStrategy.WHEN_MISSING)
                                        .build())
                .options(OllamaChatOptions.builder()
                                .model("bespoke-minicheck")
                                .temperature(0.0d)
                                .topP(1d)
                                .build())
                .build();
        var chatClientBuilder = ChatClient.builder(chatModel)
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build());
        var evaluator = FactCheckingEvaluator.builder(chatClientBuilder).build();
        var evalRequest = new EvaluationRequest(List.of(new Document("Brasil won the soccer world championship in 2002.")), "Soccer world champion 2002: " + response);
        var evalResponse = evaluator.evaluate(evalRequest);

        logger.info(evalResponse.toString());
        Assertions.assertTrue(evalResponse.isPass());
    }

}

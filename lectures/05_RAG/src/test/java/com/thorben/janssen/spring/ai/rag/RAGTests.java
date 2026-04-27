package com.thorben.janssen.spring.ai.rag;

import com.thorben.janssen.spring.ai.rag.service.ChatController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

@SpringBootTest
class RAGTests {

	private static final Logger logger = LoggerFactory.getLogger(RAGTests.class);

	@Autowired
    private ChatController chatController;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

	@Test
	void test() {
//        var question = "Can Spring AI stream the result?";
        var question = "Kann Spring AI die Antwort streamen?";
        var response = chatController.chat(question).collect(Collectors.joining()).block();
        logger.info(response);

        Assertions.assertNotNull(response);
    }
}

package com.thorben.janssen.spring.ai.memory;

import com.thorben.janssen.spring.ai.memory.jdbc.service.ChatController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
class SpringAiTests {

	private static final Logger logger = LoggerFactory.getLogger(SpringAiTests.class);

	@Autowired
    private ChatController chatController;

	@Test
	void sameConversation() {
        var conversationId = UUID.randomUUID();
        var intro = "Hey, my name is Thorben";
        var response = chatController.chat(intro,  conversationId).collect(Collectors.joining()).block();
		logger.info(response);
        var question = "What's my name";
        response = chatController.chat(question,  conversationId).collect(Collectors.joining()).block();
        logger.info(response);
        Assertions.assertTrue(response.contains("Thorben"));
	}

    @Test
    void differentConversation() {
        var intro = "Hey, my name is Thorben";
        var response = chatController.chat(intro,  UUID.randomUUID()).collect(Collectors.joining()).block();
        logger.info(response);
        var question = "What's my name";
        response = chatController.chat(question,  UUID.randomUUID()).collect(Collectors.joining()).block();
        logger.info(response);
        Assertions.assertFalse(response.contains("Thorben"));
    }
}

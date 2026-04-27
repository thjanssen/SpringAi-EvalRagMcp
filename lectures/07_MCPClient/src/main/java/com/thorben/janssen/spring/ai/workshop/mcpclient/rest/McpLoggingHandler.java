package com.thorben.janssen.spring.ai.workshop.mcpclient.rest;

import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpLogging;
import org.springframework.stereotype.Component;

@Component
public class McpLoggingHandler {

    private static final Logger logger = LoggerFactory.getLogger(McpLoggingHandler.class);

    @McpLogging(clients = "server1")
    public void handleLoggingMessage(McpSchema.LoggingMessageNotification notification) {
        logger.info("Received log: " + notification.level() +
                " - " + notification.data());
    }
}

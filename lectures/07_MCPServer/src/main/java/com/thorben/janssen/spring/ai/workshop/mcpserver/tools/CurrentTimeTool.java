package com.thorben.janssen.spring.ai.workshop.mcpserver.tools;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.logging.Logger;

@Component
public class CurrentTimeTool {

    private static Logger logger = Logger.getLogger(CurrentTimeTool.class.getName());

    @McpTool(name = "getCurrentTime",
            description = "Get the current time in the provided timezone.")
    public String getCurrentTime(@McpToolParam(description = "The timezone for the current time") String zoneId) {
        logger.info("### getCurrentTime ###");
        return ZonedDateTime.now(TimeZone.getTimeZone(zoneId).toZoneId()).toString();
    }

    @McpTool(name = "getZoneId",
            description = "Get the timezone")
    public ZoneId getZoneId(McpSyncRequestContext context) {
        logger.info("### getZoneId ###");
        context.info("### getZoneId ###");
        return ZoneId.systemDefault();
    }
}

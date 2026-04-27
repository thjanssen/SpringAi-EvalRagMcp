package com.thorben.janssen.spring.ai.workshop.mcpserver.tools;

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

    @Tool(name = "getCurrentTime",
            description = "Get the current time in the provided timezone.")
    public String getCurrentTime(@ToolParam(description = "The timezone for the current time") String zoneId) {
        logger.info("### getCurrentTime ###");
        return ZonedDateTime.now(TimeZone.getTimeZone(zoneId).toZoneId()).toString();
    }

    @Tool(name = "getZoneId",
            description = "Get the timezone")
    public ZoneId getZoneId() {
        logger.info("### getZoneId ###");
        return ZoneId.systemDefault();
    }
}

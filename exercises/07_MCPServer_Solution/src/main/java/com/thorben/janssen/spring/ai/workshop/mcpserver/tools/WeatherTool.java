package com.thorben.janssen.spring.ai.workshop.mcpserver.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.logging.Logger;

@Component
public class WeatherTool {

    private static Logger logger = Logger.getLogger(WeatherTool.class.getName());

    @Tool(name = "getCurrentWeather",
            description = "Gets the current weather at the provided location.")
    public String getCurrentTime(@ToolParam(description = "The location for which you want to get the weather") String location) {
        logger.info("### getCurrentWeather ###");
        switch (location) {
            case "Palma de Mallorca":
                return "sunny, 30°C";
            case "Paderborn, Germany":
                return "cloudy, 10°C";
            default:
                return "no information";
        }
    }
}

package com.thorben.janssen.spring.ai.workshop.mcpserver.tools;

import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class WeatherTool {

    private static Logger logger = Logger.getLogger(WeatherTool.class.getName());

    public String getCurrentTime(String location) {
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

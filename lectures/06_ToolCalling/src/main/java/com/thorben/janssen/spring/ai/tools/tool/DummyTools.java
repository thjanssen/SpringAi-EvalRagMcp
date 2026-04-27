package com.thorben.janssen.spring.ai.tools.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class DummyTools {

    private static Logger logger = Logger.getLogger(CurrentTimeTool.class.getName());

    @Tool(name = "getChessWorldChampion",
            description = "Gets the current chess world champion in classical chess.")
    public String getChessWorldChampion() {
        logger.info("### getChessWorldChampion ###");
        return "Gukesh Dommaraju";
    }

    @Tool(name = "sum",
            description = "Calculates the sum of the provided values.")
    public Double sum(List<Double> numbers) {
        double result = 0.0;
        for (Double number : numbers) {
            result += number;
        }
        return result;
    }
}

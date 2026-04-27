package com.thorben.janssen.spring.ai.workshop.mcpserver;

import com.thorben.janssen.spring.ai.workshop.mcpserver.tools.WeatherTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiMcpServer {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiMcpServer.class, args);
	}

}

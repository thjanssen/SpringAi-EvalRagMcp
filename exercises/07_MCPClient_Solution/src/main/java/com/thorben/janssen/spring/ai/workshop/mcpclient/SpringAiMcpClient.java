package com.thorben.janssen.spring.ai.workshop.mcpclient;

import org.springframework.ai.tool.toolsearch.ToolIndex;
import org.springframework.ai.tool.toolsearch.index.regex.RegexToolIndex;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiMcpClient {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiMcpClient.class, args);
	}

	@Bean
    ToolIndex toolIndex() {
		return new RegexToolIndex();  // similarity threshold
	}
}

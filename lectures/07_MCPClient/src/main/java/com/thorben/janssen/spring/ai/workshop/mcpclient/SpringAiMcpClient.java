package com.thorben.janssen.spring.ai.workshop.mcpclient;

import org.springaicommunity.tool.search.ToolSearcher;
import org.springaicommunity.tool.searcher.RegexToolSearcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiMcpClient {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiMcpClient.class, args);
	}

	@Bean
    ToolSearcher toolSearcher() {
		return new RegexToolSearcher();  // similarity threshold
	}
}

package com.thorben.janssen.spring.ai.tools;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springaicommunity.tool.search.ToolSearcher;
import org.springaicommunity.tool.searcher.RegexToolSearcher;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Push
@SpringBootApplication
public class SpringAiTool implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiTool.class, args);
	}

	@Bean
	ToolSearcher toolSearcher() {
		return new RegexToolSearcher();  // similarity threshold
	}
}

package com.thorben.janssen.spring.ai.workshop.test;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.DockerModelRunnerContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    OllamaContainer minicheckContainer() throws IOException, InterruptedException {
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LoggerFactory.getLogger(TestcontainersConfiguration.class));

        // Startet ein Image mit Ollama ohne installiertes Modell
//        var ollamaContainer = new OllamaContainer("ollama/ollama");
//        ollamaContainer.withLogConsumer(logConsumer);
//        ollamaContainer.start();
//        // Installiert das bespoke-minicheck innerhalb des Containers
//        ollamaContainer.execInContainer("ollama", "pull", "bespoke-minicheck");
//        // Optional: Erstellt ein wiederverwendbares Image dieses Containers
//        ollamaContainer.commitToImage("ollama/bespoke-minicheck");


        // Startet ein vorher erstelltes Image mit Ollama und bespoke-mini
        var ollamaContainer = new OllamaContainer(DockerImageName.parse("ollama/bespoke-minicheck").asCompatibleSubstituteFor("ollama/ollama"));
        ollamaContainer.withLogConsumer(logConsumer);
        ollamaContainer.start();

        return ollamaContainer;
    }

    @Bean
    OllamaContainer qwen3Container() throws IOException, InterruptedException {
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LoggerFactory.getLogger(TestcontainersConfiguration.class));

        // Startet ein Image mit Ollama ohne installiertes Modell
//        var ollamaContainer = new OllamaContainer("ollama/ollama");
//        ollamaContainer.withLogConsumer(logConsumer);
//        ollamaContainer.start();
//        // Installiert das qwen3 innerhalb des Containers
//        ollamaContainer.execInContainer("ollama", "pull", "qwen3:8b");
//        // Optional: Erstellt ein wiederverwendbares Image dieses Containers
//        ollamaContainer.commitToImage("ollama/qwen3-8b");

        // Startet ein vorher erstelltes Image mit Ollama und qwen3
        var ollamaContainer = new OllamaContainer(DockerImageName.parse("ollama/qwen3-8b").asCompatibleSubstituteFor("ollama/ollama"));
        ollamaContainer.withLogConsumer(logConsumer);
        ollamaContainer.start();

        return ollamaContainer;
    }

    @Bean
    DynamicPropertyRegistrar properties(@Qualifier("qwen3Container") OllamaContainer ollamaContainer) {
        return (registrar) -> {
            registrar.add("spring.ai.ollama.base-url", ollamaContainer::getEndpoint);
            registrar.add("spring.ai.ollama.chat.model", () -> "qwen3:8b");
        };
    }
}

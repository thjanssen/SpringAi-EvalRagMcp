package com.thorben.janssen.spring.ai.rag.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatController {

    private static final String SYSTEM_PROMPT = """
        You are a friendly and helpful senior Java developer.
        """;


    private final ChatClient.Builder chatClientBuilder;
    private final VectorStore vectorStore;

    public ChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClientBuilder = chatClientBuilder;
        this.vectorStore = vectorStore;
    }

    public Flux<String> chat1(String message) {
        var chatClient = chatClientBuilder
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder().build(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                            .searchRequest(SearchRequest.builder().similarityThreshold(0.0d).topK(4).build())
                            .build()
                )
                .defaultSystem(SYSTEM_PROMPT)
                .build();

        return chatClient.prompt(message).stream().content();
    }

    public Flux<String> chat2(String message) {
        var chatClient = chatClientBuilder
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder().build(),
                        RetrievalAugmentationAdvisor.builder()
                                // before document retrieval
                                .queryTransformers(
                                        TranslationQueryTransformer.builder().chatClientBuilder(chatClientBuilder.clone()).targetLanguage("english").build()
                                )
                                .documentRetriever(VectorStoreDocumentRetriever.builder().similarityThreshold(0.0d).vectorStore(vectorStore).build())
                                // query augmenter
                                .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(false).build())
                                .build()
                )
                .defaultSystem(SYSTEM_PROMPT)
                .build();

        return chatClient.prompt(message).stream().content();
    }
}

package ru.work.xmlexchange.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.work.xmlexchange.config.WebClientConfig;

public class TestPostQueryMultipart {
    private final WebClient webClient;

    public TestPostQueryMultipart(WebClientConfig webClientConfig) {
        this.webClient = webClientConfig.createWebClient();
    }

    public Mono<String> sendMultipartRequest(String xmlContent) {
        // Создание multipart тела запроса
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(xmlContent.getBytes()) {
            @Override
            public String getFilename() {
                return "request.xml";
            }
        });

        return webClient.post()
                .uri("https://example.com/endpoint")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .doOnError(WebClientResponseException.class, ex -> System.err.println("Error response: " + ex.getResponseBodyAsString()));
    }
}

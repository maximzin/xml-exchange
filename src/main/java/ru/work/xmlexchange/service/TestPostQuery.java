package ru.work.xmlexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.work.xmlexchange.config.WebClientConfig;

import java.nio.charset.StandardCharsets;

@Service
public class TestPostQuery {

    private final WebClient webClient;

    public TestPostQuery(WebClientConfig webClientConfig) {
        this.webClient = webClientConfig.createWebClient();
    }

    public Mono<String> sendPost(String xmlBody) {
        return webClient.post()
                .uri("https://proton.pm.local/send-post")
                .header("X-Data-Source", "xdatasource")
                .header("One-More-Header", "xxxxx")
                .contentType(new MediaType("application", "xml", StandardCharsets.UTF_8))
                .bodyValue(xmlBody)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class));
    }



}

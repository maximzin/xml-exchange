package ru.work.xmlexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.work.xmlexchange.config.WebClientConfig;

@Service
public class TestPostQuery {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public String getPost() {
        WebClient webClient = webClientBuilder.build();
        return webClient.post().
                .uri("https://mysite.com/api")
                .header("X-Data-Source", "xdatasource")
                .header("One-More-Header", "xxxxx")
                .toString();
    }

}

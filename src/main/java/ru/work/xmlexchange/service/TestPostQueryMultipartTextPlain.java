package ru.work.xmlexchange.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.work.xmlexchange.config.WebClientConfig;

@Service
public class TestPostQueryMultipartTextPlain {
    private final WebClient webClient;

    public TestPostQueryMultipartTextPlain(WebClientConfig webClientConfig) {
        this.webClient = webClientConfig.createWebClient();
    }

    public Mono<String> sendMultipartRequest(String xmlContent) {
        // Создание multipart тела запроса
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("formData", xmlContent)
                .header("Content-Disposition", "form-data; name=MessageText")
                //.header("Content-Type", "text/plain");
                .contentType(MediaType.TEXT_PLAIN);
        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("file", new ByteArrayResource(xmlContent.getBytes()) {
//            @Override
//            public String getFilename() {
//                return "request.xml";
//            }
//        });

        return webClient.post()
                .uri("https://proton.pm.local/send-post")
                .header("X-Data-Source", "xdatasource")
                .header("One-More-Header", "xxxxx")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBody))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response: " + response))
                .doOnError(WebClientResponseException.class, ex -> System.err.println("Error response: " + ex.getResponseBodyAsString()));
    }
}

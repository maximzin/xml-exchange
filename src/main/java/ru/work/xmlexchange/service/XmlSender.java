package ru.work.xmlexchange.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Path;

@Service
public class XmlSender {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void uploadFileAsync(Path filePath, String targetUrl) {
        WebClient webClient = webClientBuilder.build();

        FileSystemResource fileResource = new FileSystemResource(filePath.toFile());

        webClient.post()
                .uri(targetUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", fileResource))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    System.out.println("File uploaded successfully: " + response);
                })
                .doOnError(error -> {
                    System.err.println("Error uploading file: " + error.getMessage());
                })
                .subscribe();
    }
}
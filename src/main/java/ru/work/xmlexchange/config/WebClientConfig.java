package ru.work.xmlexchange.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;


public class WebClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

    public WebClient createWebClient() {
        return WebClient.builder()
                .filter(logRequest())
                .filter(logResponse())
                .build();

    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));

            return Mono.just(clientRequest.body())
                    .flatMap(body -> {
                        logger.info("Request Body: {}", body);
                        ClientRequest newRequest = ClientRequest.from(clientRequest)
                                .body(body)
                                .build();
                        return next.exchange(newRequest);
                    });
        };
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Response: {}", clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
            return Mono.just(clientResponse);
        });
    }

    //Тут логирование с multipart/form-data
//    private ExchangeFilterFunction logRequest() {
//        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
//            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
//            clientRequest.headers()
//                    .forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
//
//            if (clientRequest.headers().getContentType() != null &&
//                clientRequest.headers().getContentType().includes(MediaType.MULTIPART_FORM_DATA)) {
//                logger.info("Multipart form data content");
//            } else {
//                return clientRequest.body().toMono(String.class)
//                        .doOnNext(body -> logger.info("Request Body: {}", body))
//                        .then(Mono.just(clientRequest));
//            }
//
//            return Mono.just(clientRequest);
//        });
//    }
//
//    private ExchangeFilterFunction logResponse() {
//        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
//            logger.info("Response: {}", clientResponse.statusCode());
//            clientResponse.headers().asHttpHeaders()
//                    .forEach((name, values) -> values.forEach(value -> logger.info("{}: {}", name, value)));
//
//            return clientResponse.bodyToMono(String.class)
//                    .doOnNext(body -> logger.info("Response Body: {}", body))
//                    .then(Mono.just(clientResponse));
//        });
//    }


}

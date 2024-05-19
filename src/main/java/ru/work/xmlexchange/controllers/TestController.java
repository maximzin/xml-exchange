package ru.work.xmlexchange.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.work.xmlexchange.service.TestPostQueryMultipart;

@RestController
public class TestController {

    private final TestPostQueryMultipart testPostQueryMultipart;

    public TestController(TestPostQueryMultipart testPostQueryMultipart) {
        this.testPostQueryMultipart = testPostQueryMultipart;
    }

    @PostMapping("/send-multipart-request")
    public Mono<String> sendMultipartRequest(@RequestBody String xmlContent) {
        return testPostQueryMultipart.sendMultipartRequest(xmlContent);
    }
}
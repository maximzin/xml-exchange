package ru.work.xmlexchange.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import reactor.core.publisher.Mono;
import ru.work.xmlexchange.service.TestPostQuery;
import ru.work.xmlexchange.service.TestPostQueryMultipart;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class TestController {

    private final TestPostQuery testPostQuery;

    public TestController(TestPostQuery testPostQuery) {
        this.testPostQuery = testPostQuery;
    }

    @GetMapping("/page")
    public String testPage() {
        return "hey";
    }

    @PostMapping("/send-post")
    public ResponseEntity<String> sendPost(@RequestBody String xmlContent) {
        System.out.println("Incoming xml: " + xmlContent);
        HttpHeaders headers = new HttpHeaders();
        headers.add("header1", "heeey");
        return new ResponseEntity<>(xmlContent, headers, HttpStatus.OK);
    }

    //Если мы принимаем multipart/form-data с файлом
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Проверяем, что файл не пустой
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty file");
            }

            // Получаем содержимое файла как строку
            String xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            // Здесь можно добавить обработку XML содержимого

            // Возвращаем успешный ответ
            return ResponseEntity.ok("File received: " + xmlContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read file");
        }
    }

    //Если мы принимаем multipart/form-data с текстом
    @PostMapping(value = "/upload2", consumes = "multipart/form-data")
    public ResponseEntity<String> handleFileUpload2(@RequestParam("text") String text) {
        System.out.println(text);
        // Возвращаем успешный ответ
        //return ResponseEntity.ok("File received: " + text);
        return ResponseEntity.ok("File received: " + text);
    }
}
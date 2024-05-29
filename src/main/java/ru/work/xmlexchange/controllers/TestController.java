package ru.work.xmlexchange.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import reactor.core.publisher.Mono;
import ru.work.xmlexchange.service.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    private final XmlParser xmlParser;
    private final XmlGetFromDb xmlGetFromDb;
    private final XmlGoToDb xmlGoToDb;
    private final XmlResponse xmlResponse;

    public TestController(XmlParser xmlParser, XmlGetFromDb xmlGetFromDb, XmlGoToDb xmlGoToDb, XmlResponse xmlResponse) {
        this.xmlParser = xmlParser;
        this.xmlGetFromDb = xmlGetFromDb;
        this.xmlGoToDb = xmlGoToDb;
        this.xmlResponse = xmlResponse;
    }

    //Тут учимся парсить и создавать XML файлы для ответных сообщений со статусом
    @GetMapping("/standart")
    public String testPage() throws IOException, ParserConfigurationException, SAXException {
        List<String> xmlList = xmlGetFromDb.getXml();
        if (!xmlList.isEmpty()) {
            return xmlParser.parseResponse(xmlList.get(0));
        }
        return "no xml";
    }


    //Приём Post запросов с Content-Type=text/plain
    //ожидается, что сюда будут доходить только статусные сообщения
    //будет ли сюда что-то приниматься: МЫ НЕ ЗНАЕМ
    //в примерах сообщения с таким типом приходят только как response
    @PostMapping(value ="/send-post", consumes = "application/xml")
    public ResponseEntity<String> sendPost(@RequestHeader Map<String, String> getHeaders, @RequestBody String getXml) throws ParserConfigurationException, TransformerException {
        //Выведем в консоль
        getHeaders.forEach((key, value) -> System.out.println("header: " + key + ": " + value));
        System.out.println("Incoming xml: " + getXml);

        //Засунем это в нашу таблицу SQL tXml_incoming
        xmlGoToDb.insertXml(getXml);

        //Начинаем парсить ответ от ШОД
        try {
            String status = xmlParser.parseStatusMessage(getXml);
            status = (status != null) ? status : "no info";
            System.out.println("status: " + status);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        //Готовим ответ ШОДу с инфой, что у нас всё дошло
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");

        //Создаем xml со статус кодом 1000 (успешно)
        String xmlResp = xmlResponse.createXmlResponse(1000);
        //Возвращаем статус OK
        return ResponseEntity
                .status(HttpStatus.OK)
                .
                .body(xmlResp);
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

    //Если мы принимаем multipart/form-data с text/plain
    @PostMapping(value = "/send-post", consumes = "multipart/form-data")
    public ResponseEntity<String> handleFileUpload3(@RequestParam("MessageText") String xmlData) {
        System.out.println(xmlData);;
        // Возвращаем успешный ответ
        //return ResponseEntity.ok("File received: " + text);
        return ResponseEntity.ok().body("ok");
    }
}
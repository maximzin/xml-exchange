package ru.work.xmlexchange.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/send")
public class sendController {
    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public sendController(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<String> send() {

        //Получаем XML в формате строки
        List<String> listXml = jdbcTemplate.queryForList("SELECT TOP 1 xml FROM tXml", String.class);
        String xml = listXml.get(0);

        //Формируем сам запрос, базу
        HttpHeaders headers = new HttpHeaders();


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.
        return
    }
}

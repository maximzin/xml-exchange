package ru.work.xmlexchange.service;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class XmlGoToDb {

    final DataSource dataSource;
    final JdbcTemplate jdbcTemplate;

    public XmlGoToDb(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertXml(String xml) {

        try {
            jdbcTemplate.update("INSERT INTO tXml_incoming ([xml]) SELECT ?", xml);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

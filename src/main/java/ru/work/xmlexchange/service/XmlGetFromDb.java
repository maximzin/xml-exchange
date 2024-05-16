package ru.work.xmlexchange.service;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlGetFromDb {

    final DataSource dataSource;
    final JdbcTemplate jdbcTemplate;

    public XmlGetFromDb(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getXml() {
        int countXml;
        String xml;
        List<String> xmlList = new ArrayList<>();
        try {
            countXml = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tXml", Integer.class);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        if (countXml != 0) {
            try {
                for (int i = 1; i <= countXml; i++) {
                    xml = jdbcTemplate.queryForObject(String.format("SELECT xml FROM tXml WHERE id = (%s)", i), String.class);
                    xmlList.add(xml);
                }
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return xmlList;
    }
}

package ru.work.xmlexchange.service;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                    try {
                        assert xml != null;
                        saveXmlOnDisk(xml, i);
                    }
                    catch (IOException e) {
                        throw new IOException(e);
                    }


                }
            } catch (DataAccessException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return xmlList;
    }

    private void saveXmlOnDisk(String xml, int count) throws IOException {
        Calendar calendar = Calendar.getInstance();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());


        File newDir = new File(String.format("D:\\Работа\\xml\\%s", date));
        if (!newDir.exists()){
            newDir.mkdirs();
        }

        String path = String.format("D:\\Работа\\xml\\%s\\xml%s.xml", date, count);

        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(xml.getBytes());
        }
    }
}

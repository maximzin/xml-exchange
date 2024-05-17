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
import java.util.*;

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
        String idXml;
        String xml;

        List<String> xmlList = new ArrayList<>();
        try {
            countXml = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tXml", Integer.class);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        if (countXml != 0) {
            try {
                //Идёт перебор всех строк в таблице tXml, подразумевается, что значения id идут начиная с 1
                for (int i = 1; i <= countXml; i++) {
                    idXml = jdbcTemplate.queryForObject("SELECT TOP 1 id FROM tXml ORDER BY createDate", String.class);
                    xml = jdbcTemplate.queryForObject("SELECT TOP 1 xml FROM tXml ORDER BY createDate", String.class);
                    xmlList.add(xml);
                    try {
                        assert xml != null;

                        //Опционально: сохранять ли на жесткий диск XML файлы
                        saveXmlOnDisk(idXml, xml);

                        //Опционально: переносить в tXml_sended и удалять из основной базы tXml
                        transferXml(idXml, xml);
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

    private void saveXmlOnDisk(String idXml, String xml) throws IOException {
        Calendar calendar = Calendar.getInstance();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());


        File newDir = new File(String.format("D:\\ШОД\\xml\\%s", date));
        if (!newDir.exists()){
            newDir.mkdirs();
        }

        String path = String.format("D:\\ШОД\\xml\\%s\\%s.xml", date, idXml);

        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(xml.getBytes());
        }
    }

    private void transferXml(String idXml, String xml) {
        jdbcTemplate.execute("INSERT INTO tXml_sended (id_xml, xml) SELECT TOP 1 id, xml FROM tXml ORDER BY createDate ");
        jdbcTemplate.execute("DELETE FROM tXml WHERE id IN (SELECT TOP 1 id FROM tXml ORDER BY createDate)");
    }
}

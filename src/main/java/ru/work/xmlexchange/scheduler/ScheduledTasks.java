package ru.work.xmlexchange.scheduler;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.work.xmlexchange.service.XmlGetFromDb;
import ru.work.xmlexchange.service.XmlSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduledTasks {

    //final XmlSender xmlSender;
    final XmlGetFromDb xmlGetFromDb;


    @Autowired
    public ScheduledTasks(XmlGetFromDb xmlGetFromDb) {
        this.xmlGetFromDb = xmlGetFromDb;
        //this.xmlSender = xmlSender;
    }

    @Scheduled(fixedRate = 6000) // 60000 ms = 1 minute
    public void scheduleSendingXml() {
        List<String> xmlList = xmlGetFromDb.getXml();
        for (String xml : xmlList) {
            System.out.println("Sending xml: " + xml);
        }

        //Path filePath = Paths.get("path/to/your/file.txt");
        //String targetUrl = "http://example.com/upload";
        //xmlSender.uploadFileAsync(filePath, targetUrl);
    }
}
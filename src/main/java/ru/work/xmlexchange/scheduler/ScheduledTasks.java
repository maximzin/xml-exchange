package ru.work.xmlexchange.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Mono;
import ru.work.xmlexchange.service.TestPostQuery;
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

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    //final XmlSender xmlSender;
    final XmlGetFromDb xmlGetFromDb;
    final TestPostQuery testPostQuery;


    @Autowired
    public ScheduledTasks(XmlGetFromDb xmlGetFromDb, TestPostQuery testPostQuery) {
        this.xmlGetFromDb = xmlGetFromDb;
        this.testPostQuery = testPostQuery;
        //this.xmlSender = xmlSender;
    }

    @Scheduled(fixedRate = 6000) // 60000 ms = 1 minute
    public void scheduleSendingXml() {

        List<String> xmlList = xmlGetFromDb.getXml();
        if (!xmlList.isEmpty()) {
            for (String xml : xmlList) {
                System.out.println("Sending xml: " + xml);
                Mono<String> postQuery = testPostQuery.sendPost(xml);
                System.out.println("Post query: " + postQuery.block());
            }
        }




        //Path filePath = Paths.get("path/to/your/file.txt");
        //String targetUrl = "http://example.com/upload";
        //xmlSender.uploadFileAsync(filePath, targetUrl);
    }
}
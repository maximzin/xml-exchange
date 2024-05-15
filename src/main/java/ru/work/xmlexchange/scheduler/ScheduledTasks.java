package ru.work.xmlexchange.scheduler;

import ru.work.xmlexchange.service.XmlSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableScheduling
public class ScheduledTasks {

    final XmlSender xmlSender;

    @Autowired
    public ScheduledTasks(XmlSender xmlSender) {
        this.xmlSender = xmlSender;
    }

    @Scheduled(fixedRate = 60000) // 60000 ms = 1 minute
    public void scheduleFileUpload() {
        Path filePath = Paths.get("path/to/your/file.txt");
        String targetUrl = "http://example.com/upload";
        xmlSender.uploadFileAsync(filePath, targetUrl);
    }
}
package com.test.dailyreport;

import com.test.dailyreport.service.ReportRunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;

@SpringBootApplication
public class DailySummaryReport implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DailySummaryReport.class);

    @Autowired
    private ReportRunService reportRunService;

    public static void main(String[] args) throws Exception {

        SpringApplication app = new SpringApplication(DailySummaryReport.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        SpringApplication.run(DailySummaryReport.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("Started creating Daily Summary Report !!!");
        reportRunService.processFile();
        exit(0);
    }
}
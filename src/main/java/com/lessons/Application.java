package com.lessons;

import com.lessons.services.TestRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application implements CommandLineRunner
{
    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    public static void main( String[] args ) throws Exception {
        logger.debug("main() started.");

        // Start up Spring Boot but disable the banner
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext appContext = app.run(args);


        // Get a reference to the TestRecordService
        TestRecordService testRecordService = (TestRecordService) appContext.getBean("com.lessons.services.TestRecordService");

        // delete, create the mapping, and insert records
        testRecordService.overwriteMappingWithRecords();

        logger.debug("App finished.");
    }


    @Override
    public void run(String... args) throws Exception {

    }
}

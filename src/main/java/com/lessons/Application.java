package com.lessons;

import com.lessons.services.ElasticSearchService;
import com.lessons.utilities.SpringAppContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application
{
    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    public static void main( String[] args ) throws Exception {
        logger.debug("main() started.");

        // Start up Spring Boot but disable the banner
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

        String csvInputFile       = "/home/adam/Desktop/project/dcsa/metrics_revisedv1.csv";
        String bulkOutputFilePath = "/home/adam/Desktop/project/dcsa/bulk.json";
        String indexName = "dcsa_metrics";

        // Use the ElasticSearchService to generate a bulk json file
        ElasticSearchService elasticSearchService = (ElasticSearchService) SpringAppContextUtils.getBean("com.lessons.services.ElasticSearchService");
        elasticSearchService.generateBulkJsonFile(indexName, csvInputFile, bulkOutputFilePath);

        logger.debug("App finished.");
    }


}

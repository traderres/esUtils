package com.lessons.services;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.lessons.models.DcsaDTO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Service("com.lessons.services.ElasticSearchService")
public class ElasticSearchService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);


    private ObjectMapper objectMapper;

    public ElasticSearchService() {
        logger.debug("Constructor called");
    }

    @PostConstruct
    public void init() {
        logger.debug("init() called.");

        this.objectMapper = new ObjectMapper();

        // Tell the object mapper to convert Objects to snake case
        // For example.  object.getPersonName --> "person_name" in the json
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        // Escape non-nulls
        this.objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

        // Tell the object mapper to set dates to this format:
        DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.objectMapper.setDateFormat(outputDateFormat);
    }

    /**
     * Open the csv file
     * Loop through the csv file one line at a time
     *   a) Get the
     * @param csvInputFile
     * @param bulkOutputFilePath
     */
    public void generateBulkJsonFile(String indexName, String csvInputFile, String bulkOutputFilePath) throws Exception {
        logger.debug("generateBulkJsonFile()  csvInputFile={}  bulkOutputFilePath={}", csvInputFile, bulkOutputFilePath);


        File inputFile = new File(csvInputFile);

        List<String> lines = FileUtils.readLines(inputFile, "UTF-8");

        if ((lines != null) && (lines.size() < 2)) {
            throw new RuntimeException("Critical Error:  The input file has fewer than 2 lines in it.");
        }


        // Remove the header line
        lines.remove(0);

        StringBuilder sbBulkOutput = new StringBuilder();

        // Loop through the remaining lines
        int fileNumber=1;
        int i=0;
        for (String line: lines) {
            i++;
            DcsaDTO dto = new DcsaDTO(line);

            // Convert the DTO to JSON
            String jsonForObject = objectMapper.writeValueAsString(dto);

            // Use the JSON to create a  2-line bulk index string
            sbBulkOutput.append("{ \"index\": { \"_index\": \"" + indexName + "\", \"_type\": \"record\" }}\n")
                            .append(jsonForObject)
                            .append("\n");

            if ((i % 50000) == 0) {

                String outputFilePath = bulkOutputFilePath + fileNumber;
                logger.debug("Creating this file:   {}", outputFilePath);
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
                    bw.append(sbBulkOutput.toString());
                    bw.flush();
                }

                fileNumber++;

                // Clearout the string builder
                sbBulkOutput.setLength(0);

            }
        }

        // Write the StringBuilder to the output file
        if (sbBulkOutput.length() > 1) {
            String outputFilePath = bulkOutputFilePath + fileNumber;
            logger.debug("Creating this file:   {}", outputFilePath);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
                bw.append(sbBulkOutput.toString());
                bw.flush();
            }
        }

        logger.debug("generateBulkJsonFile()  finished");

    }
}

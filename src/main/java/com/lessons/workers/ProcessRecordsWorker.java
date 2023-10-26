package com.lessons.workers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lessons.models.RecordDTO;
import com.lessons.services.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class ProcessRecordsWorker implements Callable<String> {
    private static final Logger logger = LoggerFactory.getLogger(ProcessRecordsWorker.class);

    private final ElasticSearchService elasticSearchService;
    private final List<RecordDTO>      recordsToAdd;
    private final String               indexName;
    private final ObjectMapper         objectMapper;
    private final int                  workerNumber;

    public ProcessRecordsWorker(ElasticSearchService aElasticSearchService, List<RecordDTO> aRecordsToAdd, ObjectMapper aObjectMapper, String aIndexName, int aWorkerNumber) {
        this.elasticSearchService = aElasticSearchService;
        this.recordsToAdd = aRecordsToAdd;
        this.objectMapper = aObjectMapper;
        this.indexName = aIndexName;
        this.workerNumber = aWorkerNumber;
    }


    @Override
    public String call() throws Exception {
        try {
            logger.debug("Worker {} started", this.workerNumber);

            // Sleep for 2 seconds
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            StringBuilder bulkJsonRequest = new StringBuilder();

            for (RecordDTO recordDTO : this.recordsToAdd) {
                String line1 = "{ \"index\": { \"_index\": \"" + this.indexName + "\",  \"retry_on_conflict\" : 5, \"_id\": " + recordDTO.getId() + " }}\n";

                // Use the objectMapper to convert this ReportDTO object --> JSON
                String line2 = objectMapper.writeValueAsString(recordDTO) + "\n";

                bulkJsonRequest.append(line1);
                bulkJsonRequest.append(line2);
            }

            // Submit the Bulk Request to ElasticSearch
            logger.debug("Worker {} about to write to ES...", this.workerNumber);
            elasticSearchService.bulkUpdate(bulkJsonRequest.toString(), true);
            logger.debug("Worker {} successfully wrote to ES...", this.workerNumber);

            return null;
        }
        catch (Exception e) {
            logger.error("Error in worker " + this.workerNumber, e);

            RuntimeException re = new RuntimeException(e);
            re.setStackTrace(e.getStackTrace());
            throw re;
        }
    }

}

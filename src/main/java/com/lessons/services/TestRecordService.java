package com.lessons.services;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.lessons.models.RecordDTO;
import com.lessons.workers.ProcessRecordsWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@Service("com.lessons.services.TestRecordService")
public class TestRecordService {
    private static final Logger logger = LoggerFactory.getLogger(TestRecordService.class);

    private final List<String> randomFirstNames = Arrays.asList("Adam", "Ben", "Peter", "Justin", "Suzanne", "Joshua", "Joel", "Sheila", "William", "Samuel", "Bill", "George", "John", "Rachel", "Leia", "Abraham", "Isaac", "Kevin", "Kimberly", "Leslie", "Joseph");
    private final List<String> randomLastNames = Arrays.asList("Smith", "Resnick", "Knight", "Jackson", "Ford", "Fisher", "Washington", "Lincoln", "Carter", "Clinton", "Asomiv", "Adams", "Drucker", "DeMarco", "Collins", "Anderson");


    private final int    TOTAL_RECORDS_TO_CREATE = 1000000;
    private final int    TOTAL_WORKER_THREADS = 100;
    private final String ES_MAPPING_NAME = "big-mapping";

    private ObjectMapper objectMapper;
    private final Random random = new Random();

    @Resource
    private ElasticSearchService elasticSearchService;


    @PostConstruct
    public void init() {
        this.objectMapper = new ObjectMapper();

        // Tell the object mapper to convert Objects to snake case
        // For example.  object.getPersonName --> "person_name" in the json
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // Escape non-nulls
        this.objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // Tell the object mapper to set dates to this format:
        DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.objectMapper.setDateFormat(outputDateFormat);
    }



    public void overwriteMappingWithRecords() throws Exception {
        // Delete the index if it already exists
        elasticSearchService.deleteIndexIfExists(ES_MAPPING_NAME);

        // Construct the path of the file  (actually stored in common-backend/src/main/resources)
        String mappingFilename = "/es/big-mapping.json";

        // Get the json mapping as a string
        String jsonMapping = elasticSearchService.readFileInClasspathToString(mappingFilename);

        // Create a new index
        elasticSearchService.createIndex(ES_MAPPING_NAME, jsonMapping);

        // Add the contract spec data to this index
        addDataToMapping();
    }


    /*
     * 1) Generate a list of N objects
     * 2) Loop through the N objects
     *    a. Divide it among 100 threads
     *    b. Each thread processes N/100 records
     *    c. Submit the thread
     *
     * 3) Wait for the threads to finish
     */
    private void addDataToMapping() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(this.TOTAL_WORKER_THREADS);

        // Setup the completion service (which will return a result that is a String)
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);

        int totalRecordsForThisWorker = this.TOTAL_RECORDS_TO_CREATE / this.TOTAL_WORKER_THREADS;
        int recordsToAddToLastWorker = this.TOTAL_RECORDS_TO_CREATE % this.TOTAL_WORKER_THREADS;

        for (int workerNumber=1; workerNumber<=this.TOTAL_WORKER_THREADS; workerNumber++) {

            if (workerNumber == this.TOTAL_WORKER_THREADS) {
                totalRecordsForThisWorker = totalRecordsForThisWorker + recordsToAddToLastWorker;
            }

            // Generate a list of records
            List<RecordDTO> listOfRecords = generateListOfRecords(workerNumber, totalRecordsForThisWorker);

            // Create a worker
            ProcessRecordsWorker processRecordsWorker = new ProcessRecordsWorker(elasticSearchService, listOfRecords, this.objectMapper, this.ES_MAPPING_NAME, workerNumber);

            // Submit the worker to run asynchronously
            completionService.submit(processRecordsWorker);
        }


        // Wait for all workers to finish
        for (int workerNumber=1; workerNumber<=this.TOTAL_WORKER_THREADS; workerNumber++) {
            // Block and Wait for one of the threads to finish
            Future<String> f = completionService.take();

            // One of the threads has completed
            String sResults = f.get();
        }

        // At this point all of the threads are finished so shutdown the executor
        executorService.shutdown();

        logger.debug("addDataToMapping() finished");
    }



    private List<RecordDTO> generateListOfRecords(int aWorkerNumber, int aTotalRecordsForThisWorker) {
        List<RecordDTO> listOfRecords = new ArrayList<>();

        for (int i=1; i<=aTotalRecordsForThisWorker; i++) {
            int id = (10000 * aWorkerNumber) + i;
            String firstName    = generateRandomFirstName();
            String lastName     = generateRandomLastName();
            String fullName = firstName + " " + lastName;
            String certUsername = firstName + "." + lastName;
            String createdDate  = generateRandomDate();
            String lastUpdatedDate = generateRandomDate();
            Integer registrationStateId = getRandomIntegerBetween(1,4);
            String  registrationStateLabel = getRegistrationLabelForId(registrationStateId);

            RecordDTO recordDTO = new RecordDTO(id, certUsername, fullName, createdDate, lastUpdatedDate, registrationStateId, registrationStateLabel);

            listOfRecords.add(recordDTO);
        }

        return listOfRecords;
    }

    private String generateRandomDate() {
        int randomYear = getRandomIntegerBetween(2018, 2023);
        int randomMonth = getRandomIntegerBetween(1, 12);
        int randomDay;

        if (randomMonth == 2) {
            // This month has 28 days
            randomDay = getRandomIntegerBetween(1, 28);
        }
        else if ((randomMonth == 9) || (randomMonth == 4) || (randomMonth == 6) || (randomMonth == 11)) {
            // The month has 30 days
            randomDay = getRandomIntegerBetween(1, 30);
        }
        else {
            // this month has 31 days
            randomDay = getRandomIntegerBetween(1, 31);
        }

        String date = String.format("%02d/%02d/%04d", randomMonth, randomDay, randomYear);
        return date;
    }


    private String generateRandomLastName() {
        int randomId = getRandomIntegerBetween(0, randomLastNames.size() - 1);
        return this.randomLastNames.get(randomId);
    }

    private String generateRandomFirstName() {
        int randomId = getRandomIntegerBetween(0, randomFirstNames.size() - 1);
        return this.randomFirstNames.get(randomId);
    }

    private String getRegistrationLabelForId(Integer aRegistrationStateId) {
        if (aRegistrationStateId == 1) {
            return "Unregistered";
        }
        else if (aRegistrationStateId == 2) {
            return "Waiting for Verification";
        }
        else if (aRegistrationStateId == 3) {
            return "Registration Approved";
        }
        else if (aRegistrationStateId == 4) {
            return "Registration Denied";
        }
        else {
            throw new RuntimeException("Invalid value in getRegistrationLabelForId():  " + aRegistrationStateId);
        }
    }


    public int getRandomIntegerBetween(int aMinNumber, int aMaxNumber)
    {
        int randomNumber = random.nextInt(aMaxNumber - aMinNumber) + aMinNumber;

        return(randomNumber);
    }

}

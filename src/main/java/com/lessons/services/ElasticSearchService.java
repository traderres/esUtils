package com.lessons.services;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.lessons.models.ErrorsDTO;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Service("com.lessons.services.ElasticSearchService")
public class ElasticSearchService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);


    private ObjectMapper objectMapper;
    private AsyncHttpClient asyncHttpClient;
    private String elasticSearchUrl = "http://localhost:9201";

    private final int ES_REQUEST_TIMEOUT_IN_MILLISECS = 90000;

    @PostConstruct
    public void init() {
        logger.debug("init() called.");

        this.objectMapper = new ObjectMapper();

        this.asyncHttpClient = new AsyncHttpClient();


        // Tell the object mapper to convert Objects to snake case
        // For example.  object.getPersonName --> "person_name" in the json
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // Escape non-nulls
        this.objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

        // Tell the object mapper to set dates to this format:
        DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.objectMapper.setDateFormat(outputDateFormat);
    }



    /**
     * Create a new ES Index
     *
     * @param aIndexName   holds the name of the new index to create
     * @param aJsonMapping holds the mapping of this index
     * @throws Exception if something bad happens
     */
    public void createIndex(String aIndexName, String aJsonMapping) throws Exception {
        logger.debug("createIndex() started.  aIndexName={}", aIndexName);

        if (StringUtils.isEmpty(aIndexName)) {
            throw new RuntimeException("The passed-in aIndexName is null or empty.");
        }

        String url = this.elasticSearchUrl + "/" + aIndexName;
        logger.debug("Going to this url:  {}", url);

        // Make a synchronous POST call to ElasticSearch to create this an index
        Response response = this.asyncHttpClient.preparePut(url)
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setBody(aJsonMapping)
                .execute()
                .get();

        if (response.getStatusCode() != 200) {
            // ElasticSearch returned a non-200 status response
            throw new RuntimeException("Error in createIndex:  ES returned a status code of " +
                    response.getStatusCode() + " with an error of: " +
                    response.getResponseBody());
        }

        logger.info("Successfully created this ES index: {}", aIndexName);
    }


    /**
     * Delete the index from ElasticSearch
     *
     * @param aIndexName holds the index name (or alias name)
     */
    public void deleteIndexAnyway(String aIndexName) throws Exception {
        if (StringUtils.isEmpty(aIndexName)) {
            throw new RuntimeException("The passed-in aIndexName is null or empty.");
        }

        // Make a synchronous POST call to delete this ES Index
        Response response = this.asyncHttpClient.prepareDelete(this.elasticSearchUrl + "/" +
                        aIndexName)
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .execute()
                .get();

        logger.debug("Successfully deleted this index: {}", aIndexName);
    }



    /**
     * Delete the index from ElasticSearch
     *
     * @param aIndexName holds the index name (or alias name)
     */
    public void deleteIndex(String aIndexName) throws Exception {
        if (StringUtils.isEmpty(aIndexName)) {
            throw new RuntimeException("The passed-in aIndexName is null or empty.");
        }

        // Make a synchronous POST call to delete this ES Index
        Response response = this.asyncHttpClient.prepareDelete(this.elasticSearchUrl + "/" +
                        aIndexName)
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .execute()
                .get();

        if (response.getStatusCode() != 200) {
            // ElasticSearch returned a non-200 status response
            throw new RuntimeException("Error in deleteIndex:  ES returned a status code of " +
                    response.getStatusCode() + " with an error of: " + response.getResponseBody());
        }

        logger.debug("Successfully deleted this index: {}", aIndexName);
    }



    public void bulkUpdate(String aBulkUpdateJson, boolean aWaitForRefresh) throws Exception {
        if (StringUtils.isEmpty(aBulkUpdateJson)) {
            throw new RuntimeException("The passed-in JSON is null or empty.");
        }

        String url = this.elasticSearchUrl + "/_bulk";
        if (aWaitForRefresh) {
            url = url + "?refresh=wait_for";
        }

        // Make a synchronous POST call to do a bulk-index request
        Response response = this.asyncHttpClient.preparePost(url)
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setBody(aBulkUpdateJson)
                .execute()
                .get();

        while (response.getStatusCode() == 429) {
            // Too many requests.  So, wait and try again
            Thread.sleep(4000);

            response = this.asyncHttpClient.preparePost(url)
                    .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                    .setHeader("accept", "application/json")
                    .setHeader("Content-Type", "application/json")
                    .setBody(aBulkUpdateJson)
                    .execute()
                    .get();
        }


        if (response.getStatusCode() != 200) {
            // ElasticSearch returned a non-200 status response
            throw new RuntimeException("Error in bulkUpdate:  ES returned a status code of " + response.getStatusCode() + " with an error of: " + response.getResponseBody());
        }

        // Examine the JSON response to see if the errors="true" flag was set
        //  1. Convert the response JSON string into an errorsDto object
        //  2. Look at the errorsDTO object.isErrors() method
        // 	NOTE:  This is substantially faster as the ErrorDTO tells Jackson to ignore the other fields
        String jsonResponse = response.getResponseBody();
        ErrorsDTO errorsDTO = objectMapper.readValue(jsonResponse, ErrorsDTO.class);

        if (errorsDTO.isErrors()) {
            // ElasticSearch returned a 200 response, but the bulk update failed
            logger.error("Error in bulkUpdate:  ES returned a status code of {} with an error of {}", response.getStatusCode(), response.getResponseBody());
            throw new RuntimeException("Error in bulkUpdate:  ES returned a status code of " + response.getStatusCode() + " with an error of: " + response.getResponseBody());
        }

    }


    public void deleteIndexIfExists(String aIndexName) throws Exception {
        if (doesIndexExist(aIndexName)) {
            // Index exists.  So, delete it
            deleteIndex(aIndexName);
        }
    }

    /**
     * Helper method to determine if the passed-in ES mapping name or alias exists
     *
     * @param aIndexName holds the ES mapping name or alias
     * @return TRUE if the passed-in index or alias exists
     */
    public boolean doesIndexExist(String aIndexName) throws Exception {

        if (StringUtils.isEmpty(aIndexName)) {
            throw new RuntimeException("The passed-in aIndexName is null or empty.");
        }

        // Make a synchronous GET call to get a list of all index names
        Response response = this.asyncHttpClient.prepareGet(this.elasticSearchUrl + "/_cat/indices/" + aIndexName)
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "text/plain")
                .execute()
                .get();

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Critical error in doesIndexExist():  ElasticSearch returned a response status code of " +
                    response.getStatusCode() + ".  Response message is " + response.getResponseBody());
        }

        // Loop through the lines of data -- looking for the passed-in index name
        String linesOfInfo = response.getResponseBody();
        if (StringUtils.isNotEmpty(linesOfInfo)) {
            String[] lines = linesOfInfo.split("\n");

            for (String line : lines) {
                String[] indexParts = line.split(" ");
                if (indexParts.length >= 3) {
                    String actualIndexName = indexParts[2];

                    if (actualIndexName.equalsIgnoreCase(aIndexName)) {
                        logger.debug("doesIndexExist() returns true for {}", aIndexName);
                        return true;
                    }
                }
            }
        }

        // The index name was not found in the system.  So, return false
        return false;

    }  // end of doesIndexExist()


    /**
     * @param aFilename holds the name of the filename to look for in the classpath
     * @return the file contents as a string
     * @throws IOException if something bad heppens
     */
    public String readFileInClasspathToString(String aFilename) throws IOException {
        try (InputStream inputStream =  ElasticSearchService.class.getResourceAsStream(aFilename)) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }

}

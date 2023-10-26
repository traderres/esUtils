package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecordDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("cert_username")
    private String certUsername;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("last_updated_date")
    private String lastUpdatedDate;

    @JsonProperty("registration_state_id")
    private Integer registrationStateId;

    @JsonProperty("registration_label")
    private String registrationLabel;


    // ----------------- Constructor & Getters ----------------

    public RecordDTO(Integer aId, String aCertUsername, String aFullName,
                     String aCreatedDate, String aLastUpdatedDate,
                     Integer aRegistrationStateId, String aRegistrationLabel) {
        this.id = aId;
        this.certUsername = aCertUsername;
        this.fullName = aFullName;
        this.createdDate = aCreatedDate;
        this.lastUpdatedDate = aLastUpdatedDate;
        this.registrationStateId = aRegistrationStateId;
        this.registrationLabel = aRegistrationLabel;
    }


    public Integer getId() {
        return id;
    }

    public String getCertUsername() {
        return certUsername;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Integer getRegistrationStateId() {
        return registrationStateId;
    }

    public String getRegistrationLabel() {
        return registrationLabel;
    }
}

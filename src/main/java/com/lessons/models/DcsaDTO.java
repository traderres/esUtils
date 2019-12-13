package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DcsaDTO {
    private static final Logger logger = LoggerFactory.getLogger(DcsaDTO.class);

    @JsonProperty("ADJ_CASE_ID")
    private String adjCaseId;

    @JsonProperty("DISS_SUBJECT_ID")
    private String disaSubjectId;

    @JsonProperty("SSN")
    private String ssn;

    @JsonProperty("CASE_ORIGINATION")
    private String caseOrigination;

    @JsonProperty("INV_TYPE_CODE")
    private String invTypeCode;

    @JsonProperty("INVESTIGATION_TYPE")
    private String investigationType;

    @JsonProperty("INV_TYPE_CODE_NORM")
    private String invTypeCodeNorm;

    @JsonProperty("SOI")
    private String soi;

    @JsonProperty("SON")
    private String son;

    @JsonProperty("EXTERNAL_INV_CASE_ID")
    private String externalInvCaseId;

    @JsonProperty("OPM_CASE_SERIOUSNESS_CODE")
    private String opmCaseSeriousnessCode;

    @JsonProperty("DATE_INITIATED")
    private Timestamp dateInitiated;

    @JsonProperty("INV_OPENED_DATE")
    private Timestamp invOpenedDate;

    @JsonProperty("INV_OPENED_DATE_LAST_MONTH")
    private Boolean invOpenedDateLastMonth;

    @JsonProperty("INV_CLOSED_DATE")
    private Timestamp invClosedDate;

    @JsonProperty("INV_CLOSED_DATE_LAST_MONTH")
    private Boolean invClosedDateLastMonth;

    @JsonProperty("CLOSED_FLAG")
    private String closedFlag;

    @JsonProperty("ADJ_CASE_TYPE_CODE")
    private String adjCaseTypeCode;

    @JsonProperty("CASE_TYPE")
    private String caseType;

    @JsonProperty("ADJ_CASE_STATUS_CODE")
    private String adjCaseStatusCode;

    @JsonProperty("ADJUDICATION_STATUS")
    private String adjudicationStatus;

    @JsonProperty("ADJ_CASE_PHASE_CODE")
    private String adjCasePhaseCode;

    @JsonProperty("CASE_PHASE")
    private String casePhase;

    @JsonProperty("DTMN_CODE")
    private String dtmnCode;

    @JsonProperty("DETERMINATION")
    private String determination;

    @JsonProperty("DTMN_LEVEL_CODE")
    private String dtmnLevelCode;

    @JsonProperty("DETERMINATION_LEVEL")
    private String determinationLevel;

    @JsonProperty("ADJUDICATOR")
    private String adjudicator;

    @JsonProperty("REVIEWER")
    private String reviewer;

    @JsonProperty("CASE_OPEN_DATE")
    private Timestamp caseOpenDate;

    @JsonProperty("CASE_CLOSE_DATE")
    private Timestamp caseCloseDate;

    @JsonProperty("CASE_ORIGINATION_CODE")
    private String caseOriginationCode;

    @JsonProperty("OWNING_DIVISION_ID")
    private String owningDivisionId;

    @JsonProperty("OWNING_DIVISION")
    private String owningDivision;

    @JsonProperty("PRIORITY_PROGRAM")
    private String priorityProgram;

    @JsonProperty("RFA_SENT_DATE")
    private Timestamp rfaSentDate;

    @JsonProperty("RFA_TYPE")
    private String rfaType;

    @JsonProperty("RFA_STATUS")
    private String rfaStatus;

    @JsonProperty("RFA_STATUS_DATE")
    private Timestamp rfaStatusDate;

    @JsonProperty("RFA_READ_DATE")
    private Timestamp rfaReadDate;

    @JsonProperty("RFA_SMO_SENT_TO")
    private String rfaSmoSentTo;

    @JsonProperty("LAST_ACTION_DATE")
    private Timestamp lastActionDate;

    @JsonProperty("EADJUDICATED")
    private String eadjudicated;

    @JsonProperty("EXTRA_COVERAGE_CODES")
    private String extraCoverageCodes;

    @JsonProperty("OWNING_SMO_NAME")
    private String owningSmoName;

    @JsonProperty("AUTO_ASSIGN_ELIGIBLE")
    private String autoAssignEligible;

    @JsonProperty("LAST_UPDATED_TIME")
    private Timestamp lastUpdatedTime;

    @JsonProperty("CASE_PROCESSED_DATE")
    private Timestamp caseProcessedDate;

    @JsonProperty("DISCONTINUED_CASE")
    private String discontinuedCase;

    @JsonProperty("CSR_TYPE")
    private String csrType;

    @JsonProperty("CASE_ASSIGNED_DATETIME")
    private Timestamp caseAssignedDateTime;

    @JsonProperty("POSITION_CODE")
    private String positionCode;

    @JsonProperty("CSR_CREATED_DATE")
    private Timestamp csrCreatedDate;

    public String getAdjCaseId() {
        return adjCaseId;
    }

    public String getDisaSubjectId() {
        return disaSubjectId;
    }

    public String getSsn() {
        return ssn;
    }

    public String getCaseOrigination() {
        return caseOrigination;
    }

    public String getInvTypeCode() {
        return invTypeCode;
    }

    public String getInvestigationType() {
        return investigationType;
    }

    public String getInvTypeCodeNorm() {
        return invTypeCodeNorm;
    }

    public String getSoi() {
        return soi;
    }

    public String getSon() {
        return son;
    }

    public String getExternalInvCaseId() {
        return externalInvCaseId;
    }

    public String getOpmCaseSeriousnessCode() {
        return opmCaseSeriousnessCode;
    }

    public Timestamp getDateInitiated() {
        return dateInitiated;
    }

    public Timestamp getInvOpenedDate() {
        return invOpenedDate;
    }

    public Boolean getInvOpenedDateLastMonth() {
        return invOpenedDateLastMonth;
    }

    public Timestamp getInvClosedDate() {
        return invClosedDate;
    }

    public Boolean getInvClosedDateLastMonth() {
        return invClosedDateLastMonth;
    }

    public String getClosedFlag() {
        return closedFlag;
    }

    public String getAdjCaseTypeCode() {
        return adjCaseTypeCode;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getAdjCaseStatusCode() {
        return adjCaseStatusCode;
    }

    public String getAdjudicationStatus() {
        return adjudicationStatus;
    }

    public String getAdjCasePhaseCode() {
        return adjCasePhaseCode;
    }

    public String getCasePhase() {
        return casePhase;
    }

    public String getDtmnCode() {
        return dtmnCode;
    }

    public String getDetermination() {
        return determination;
    }

    public String getDtmnLevelCode() {
        return dtmnLevelCode;
    }

    public String getDeterminationLevel() {
        return determinationLevel;
    }

    public String getAdjudicator() {
        return adjudicator;
    }

    public String getReviewer() {
        return reviewer;
    }

    public Timestamp getCaseOpenDate() {
        return caseOpenDate;
    }

    public Timestamp getCaseCloseDate() {
        return caseCloseDate;
    }

    public String getCaseOriginationCode() {
        return caseOriginationCode;
    }

    public String getOwningDivisionId() {
        return owningDivisionId;
    }

    public String getOwningDivision() {
        return owningDivision;
    }

    public String getPriorityProgram() {
        return priorityProgram;
    }

    public Timestamp getRfaSentDate() {
        return rfaSentDate;
    }

    public String getRfaType() {
        return rfaType;
    }

    public String getRfaStatus() {
        return rfaStatus;
    }

    public Timestamp getRfaStatusDate() {
        return rfaStatusDate;
    }

    public Timestamp getRfaReadDate() {
        return rfaReadDate;
    }

    public String getRfaSmoSentTo() {
        return rfaSmoSentTo;
    }

    public Timestamp getLastActionDate() {
        return lastActionDate;
    }

    public String getEadjudicated() {
        return eadjudicated;
    }

    public String getExtraCoverageCodes() {
        return extraCoverageCodes;
    }

    public String getOwningSmoName() {
        return owningSmoName;
    }

    public String getAutoAssignEligible() {
        return autoAssignEligible;
    }

    public Timestamp getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public Timestamp getCaseProcessedDate() {
        return caseProcessedDate;
    }

    public String getDiscontinuedCase() {
        return discontinuedCase;
    }

    public String getCsrType() {
        return csrType;
    }

    public Timestamp getCaseAssignedDateTime() {
        return caseAssignedDateTime;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public Timestamp getCsrCreatedDate() {
        return csrCreatedDate;
    }

    private Timestamp convertToTimestamp(String aString) {
        if (StringUtils.isEmpty(aString)) {
            return null;
        }

        if (aString.length() == 1) {
            return null;
        }


        try {

            if (aString.endsWith("PM") || (aString.endsWith("AM"))) {
                String dateWithoutMilliSeconds = aString.substring(0, aString.lastIndexOf("."));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH.mm.ss");
                Date parsedDate = dateFormat.parse(dateWithoutMilliSeconds);
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                return timestamp;
            }
            else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = dateFormat.parse(aString);
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                return timestamp;
            }
        }
        catch (Exception e) {
            logger.warn("Could not parse this date:  "  + aString, e);
            return null;
        }
    }


    private String removeDoubleQuotes(String aString) {
        return aString.replaceAll("\"", "");
    }

    public DcsaDTO(String line) throws Exception {
        String pattern = ",(?=(?:(?:[^\\\"]*\\\"){2})*[^\\\"]*$)";

        String[] fixedArray = line.split(pattern, -1);
       //  String[] fixedArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ",");

        this.adjCaseId = fixedArray[0];
        this.disaSubjectId = fixedArray[1];
        this.ssn = fixedArray[2];
        this.caseOrigination = fixedArray[3];
        this.invTypeCode = fixedArray[4];
        this.investigationType = fixedArray[5];
        this.invTypeCodeNorm = fixedArray[6];
        this.soi = fixedArray[7];
        this.son = fixedArray[8];
        this.externalInvCaseId = fixedArray[9];
        this.opmCaseSeriousnessCode = fixedArray[10];
        this.dateInitiated = convertToTimestamp(fixedArray[11]);
        this.invOpenedDate = convertToTimestamp(fixedArray[12]);
        this.invOpenedDateLastMonth = new Boolean(fixedArray[13]);
        this.invClosedDate = convertToTimestamp(fixedArray[14]);
        this.invClosedDateLastMonth = new Boolean(fixedArray[15]);
        this.closedFlag = fixedArray[16];
        this.adjCaseTypeCode = fixedArray[17];
        this.caseType = fixedArray[18];
        this.adjCaseStatusCode = fixedArray[19];
        this.adjudicationStatus = fixedArray[20];
        this.adjCasePhaseCode = fixedArray[21];
        this.casePhase = fixedArray[22];
        this.dtmnCode = fixedArray[23];
        this.determination = fixedArray[24];
        this.dtmnLevelCode = fixedArray[25];
        this.determinationLevel= fixedArray[26];
        this.adjudicator = removeDoubleQuotes(fixedArray[27]);
        this.reviewer = removeDoubleQuotes(fixedArray[28]);
        this.caseOpenDate = convertToTimestamp(fixedArray[29]);
        this.caseCloseDate = convertToTimestamp(fixedArray[30]);
        this.caseOriginationCode = fixedArray[31];
        this.owningDivisionId = fixedArray[32];
        this.owningDivision = fixedArray[33];
        this.priorityProgram = fixedArray[34];
        this.rfaSentDate = convertToTimestamp(fixedArray[35]);
        this.rfaType = fixedArray[36];
        this.rfaStatus = fixedArray[37];
        this.rfaStatusDate = convertToTimestamp(fixedArray[38]);
        this.rfaReadDate = convertToTimestamp(fixedArray[39]);
        this.rfaSmoSentTo = fixedArray[40];
        this.lastActionDate = convertToTimestamp(fixedArray[41]);
        this.eadjudicated = fixedArray[42];
        this.extraCoverageCodes = fixedArray[43];
        this.owningSmoName = fixedArray[44];
        this.autoAssignEligible = fixedArray[45];
        this.lastUpdatedTime = convertToTimestamp(fixedArray[46]);
        this.caseProcessedDate = convertToTimestamp(fixedArray[47]);
        this.discontinuedCase = fixedArray[48];
        this.csrType = fixedArray[49];
        this.caseAssignedDateTime = convertToTimestamp(fixedArray[50]);
        this.positionCode = fixedArray[51];
        this.csrCreatedDate = convertToTimestamp(fixedArray[52]);
    }
}

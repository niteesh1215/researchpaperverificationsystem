package com.kristujayanticollege.researchpaperverificationsystem.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResearchDetailsRow {
    private Long id;
    private String fullName;
    private String indexing;
    private String orcid;
    private String manuscriptTitle;
    private String journalTitle;
    private String volumeNumber;
    private String issn;
    private String hIndexByScopus;
    private String isAffiliatedToKJC; // yes or no
    private Map<String, Object> additionalDetails = new LinkedHashMap<>();

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getFullName() {
        return fullName;
    }

    @JsonProperty("name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty("indexing")
    public String getIndexing() {
        return indexing;
    }

    @JsonProperty("indexing")
    public void setIndexing(String indexing) {
        this.indexing = indexing;
    }

    @JsonProperty("orcid")
    public String getOrcid() {
        return orcid;
    }

    @JsonProperty("orcid")
    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    @JsonProperty("manuscript_title")
    public String getManuscriptTitle() {
        return manuscriptTitle;
    }

    @JsonProperty("manuscript_title")
    public void setManuscriptTitle(String manuscriptTitle) {
        this.manuscriptTitle = manuscriptTitle;
    }

    @JsonProperty("journal_title")
    public String getJournalTitle() {
        return journalTitle;
    }

    @JsonProperty("journal_title")
    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    @JsonProperty("volume_number")
    public String getVolumeNumber() {
        return volumeNumber;
    }

    @JsonProperty("volume_number")
    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    @JsonProperty("issn")
    public String getIssn() {
        return issn;
    }

    @JsonProperty("issn")
    public void setIssn(String issn) {
        this.issn = issn;
    }

    @JsonProperty("h_index_given_by_scopus")
    public String gethIndexByScopus() {
        return hIndexByScopus;
    }

    @JsonProperty("h_index_given_by_scopus")
    public void sethIndexByScopus(String hIndexByScopus) {
        this.hIndexByScopus = hIndexByScopus;
    }

    @JsonProperty("is_authorship_affiliated_to_kjc")
    public String isAffiliatedToKJC() {
        return isAffiliatedToKJC;
    }

    @JsonProperty("is_authorship_affiliated_to_kjc")
    public void setAffiliatedToKJC(String isAffiliatedToKJC) {
        this.isAffiliatedToKJC = isAffiliatedToKJC;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalDetails() {
        return additionalDetails;
    }

    @JsonAnySetter
    public void setAdditionalDetails(String key, Object value) {
        this.additionalDetails.put(key, value);
    }

    @Override
    public String toString() {
        return "ResearchDetailsRow [additionalDetails=" + additionalDetails + ", fullName=" + fullName
                + ", hIndexByScopus=" + hIndexByScopus + ", indexing=" + indexing + ", isAffiliatedToKJC="
                + isAffiliatedToKJC + ", issn=" + issn + ", journalTitle=" + journalTitle + ", manuscriptTitle="
                + manuscriptTitle + ", orcid=" + orcid + ", volumeNumber=" + volumeNumber + "]";
    }

}

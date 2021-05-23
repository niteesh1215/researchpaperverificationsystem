package com.kristujayanticollege.researchpaperverificationsystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class VerificationDetails {

    @Id
    Long researchDetailsRowId;

    boolean fullNameMatch;
    String fetchedFullName;

    boolean manuscriptTitleMatch;
    String fetchedManuscriptTitle;

    boolean journalTitleMatch;
    String fetchedJournalTitle;

    boolean volumeNumberMatch;
    String fetchedVolumeNumber;

    boolean issnMatch;
    String fetchedIssn;

    double verificationMatchPercentage;

    @Column(name = "found_url_1")
    String foundUrl1;
    @Column(name = "found_url_2")
    String foundUrl2;

    public VerificationDetails() {
        fullNameMatch = false;
        manuscriptTitleMatch = false;
        journalTitleMatch = false;
        volumeNumberMatch = false;
        issnMatch = false;
        verificationMatchPercentage = 0.0;
    }

    @JsonProperty("id")
    public Long getResearchDetailsRowId() {
        return researchDetailsRowId;
    }

    public void setResearchDetailsRowId(Long researchDetailsRowId) {
        this.researchDetailsRowId = researchDetailsRowId;
    }

    @JsonProperty("fullNameMatch")
    public boolean isFullNameMatch() {
        return fullNameMatch;
    }

    public void setFullNameMatch(boolean fullNameMatch) {
        this.fullNameMatch = fullNameMatch;
    }

    @JsonProperty("fullName")
    public String getFetchedFullName() {
        return fetchedFullName;
    }

    public void setFetchedFullName(String fetchedFullName) {
        this.fetchedFullName = fetchedFullName;
    }

    @JsonProperty("manuscriptTitleMatch")
    public boolean isManuscriptTitleMatch() {
        return manuscriptTitleMatch;
    }

    public void setManuscriptTitleMatch(boolean manuscriptTitleMatch) {
        this.manuscriptTitleMatch = manuscriptTitleMatch;
    }

    @JsonProperty("manuscriptTitle")
    public String getFetchedManuscriptTitle() {
        return fetchedManuscriptTitle;
    }

    public void setFetchedManuscriptTitle(String fetchedManuscriptTitle) {
        this.fetchedManuscriptTitle = fetchedManuscriptTitle;
    }

    @JsonProperty("journalTitleMatch")
    public boolean isJournalTitleMatch() {
        return journalTitleMatch;
    }

    public void setJournalTitleMatch(boolean journalTitleMatch) {
        this.journalTitleMatch = journalTitleMatch;
    }

    @JsonProperty("journalTitle")
    public String getFetchedJournalTitle() {
        return fetchedJournalTitle;
    }

    public void setFetchedJournalTitle(String fetchedJournalTitle) {
        this.fetchedJournalTitle = fetchedJournalTitle;
    }

    @JsonProperty("volumeNumberMatch")
    public boolean isVolumeNumberMatch() {
        return volumeNumberMatch;
    }

    public void setVolumeNumberMatch(boolean volumeNumberMatch) {
        this.volumeNumberMatch = volumeNumberMatch;
    }

    @JsonProperty("volumeNumber")
    public String getFetchedVolumeNumber() {
        return fetchedVolumeNumber;
    }

    public void setFetchedVolumeNumber(String fetchedVolumeNumber) {
        this.fetchedVolumeNumber = fetchedVolumeNumber;
    }

    @JsonProperty("issnMatch")
    public boolean isIssnMatch() {
        return issnMatch;
    }

    public void setIssnMatch(boolean issnMatch) {
        this.issnMatch = issnMatch;
    }

    @JsonProperty("issn")
    public String getFetchedIssn() {
        return fetchedIssn;
    }

    public void setFetchedIssn(String fetchedIssn) {
        this.fetchedIssn = fetchedIssn;
    }

    @JsonProperty("verificationMatchPercentage")
    public double getVerificationMatchPercentage() {
        return verificationMatchPercentage;
    }

    public void setVerificationMatchPercentage(double verificationMatchPercentage) {
        this.verificationMatchPercentage = verificationMatchPercentage;
    }

    @JsonProperty("foundAtUrls")
    public List<String> getFoundAtUrls() {
        List<String> foundUrls = new ArrayList<String>();
        foundUrls.add(this.foundUrl1);
        foundUrls.add(this.foundUrl2);

        return foundUrls;
    }

    public void setFoundAtUrls(List<String> foundUrls) {
        if (foundUrls.size() >= 1)
            this.foundUrl1 = foundUrls.get(0);
        if (foundUrls.size() >= 2)
            this.foundUrl2 = foundUrls.get(1);

    }

    @Override
    public String toString() {
        return "VerificationDetails [fetchedFullName=" + fetchedFullName + ", fetchedIssn=" + fetchedIssn
                + ", fetchedJournalTitle=" + fetchedJournalTitle + ", fetchedManuscriptTitle=" + fetchedManuscriptTitle
                + ", fetchedVolumeNumber=" + fetchedVolumeNumber + ", fullNameMatch=" + fullNameMatch + ", issnMatch="
                + issnMatch + ", journalTitleMatch=" + journalTitleMatch + ", manuscriptTitleMatch="
                + manuscriptTitleMatch + ", foundUrl1=" + foundUrl1 + ", foundUrl2=" + foundUrl2
                + ", researchDetailsRowId=" + researchDetailsRowId + ", verificationMatchPercentage="
                + verificationMatchPercentage + ", volumeNumberMatch=" + volumeNumberMatch + "]";
    }

}

package com.kristujayanticollege.researchpaperverificationsystem.model;

import javax.persistence.Entity;
import javax.persistence.Id;


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

    public VerificationDetails(){
        fullNameMatch=false;
        manuscriptTitleMatch=false;
        journalTitleMatch=false;
        volumeNumberMatch=false;
        issnMatch=false;
        verificationMatchPercentage = 0.0;
    }

    public Long getResearchDetailsRowId() {
        return researchDetailsRowId;
    }

    public void setResearchDetailsRowId(Long researchDetailsRowId) {
        this.researchDetailsRowId = researchDetailsRowId;
    }

    public boolean isFullNameMatch() {
        return fullNameMatch;
    }

    public void setFullNameMatch(boolean fullNameMatch) {
        this.fullNameMatch = fullNameMatch;
    }

    public String getFetchedFullName() {
        return fetchedFullName;
    }

    public void setFetchedFullName(String fetchedFullName) {
        this.fetchedFullName = fetchedFullName;
    }

    public boolean isManuscriptTitleMatch() {
        return manuscriptTitleMatch;
    }

    public void setManuscriptTitleMatch(boolean manuscriptTitleMatch) {
        this.manuscriptTitleMatch = manuscriptTitleMatch;
    }

    public String getFetchedManuscriptTitle() {
        return fetchedManuscriptTitle;
    }

    public void setFetchedManuscriptTitle(String fetchedManuscriptTitle) {
        this.fetchedManuscriptTitle = fetchedManuscriptTitle;
    }

    public boolean isJournalTitleMatch() {
        return journalTitleMatch;
    }

    public void setJournalTitleMatch(boolean journalTitleMatch) {
        this.journalTitleMatch = journalTitleMatch;
    }

    public String getFetchedJournalTitle() {
        return fetchedJournalTitle;
    }

    public void setFetchedJournalTitle(String fetchedJournalTitle) {
        this.fetchedJournalTitle = fetchedJournalTitle;
    }

    public boolean isVolumeNumberMatch() {
        return volumeNumberMatch;
    }

    public void setVolumeNumberMatch(boolean volumeNumberMatch) {
        this.volumeNumberMatch = volumeNumberMatch;
    }

    public String getFetchedVolumeNumber() {
        return fetchedVolumeNumber;
    }

    public void setFetchedVolumeNumber(String fetchedVolumeNumber) {
        this.fetchedVolumeNumber = fetchedVolumeNumber;
    }

    public boolean isIssnMatch() {
        return issnMatch;
    }

    public void setIssnMatch(boolean issnMatch) {
        this.issnMatch = issnMatch;
    }

    public String getFetchedIssn() {
        return fetchedIssn;
    }

    public void setFetchedIssn(String fetchedIssn) {
        this.fetchedIssn = fetchedIssn;
    }

    public double getVerificationMatchPercentage() {
        return verificationMatchPercentage;
    }

    public void setVerificationMatchPercentage(double verificationMatchPercentage) {
        this.verificationMatchPercentage = verificationMatchPercentage;
    }

    @Override
    public String toString() {
        return "VerificationDetails [fetchedFullName=" + fetchedFullName + ", fetchedIssn=" + fetchedIssn
                + ", fetchedJournalTitle=" + fetchedJournalTitle + ", fetchedManuscriptTitle=" + fetchedManuscriptTitle
                + ", fetchedVolumeNumber=" + fetchedVolumeNumber + ", fullNameMatch=" + fullNameMatch 
                + ", issnMatch=" + issnMatch + ", journalTitleMatch=" + journalTitleMatch + ", manuscriptTitleMatch="
                + manuscriptTitleMatch + ", researchDetailsRowId=" + researchDetailsRowId
                + ", verificationMatchPercentage=" + verificationMatchPercentage + ", volumeNumberMatch="
                + volumeNumberMatch + "]";
    }

}

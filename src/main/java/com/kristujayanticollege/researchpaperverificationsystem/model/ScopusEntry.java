package com.kristujayanticollege.researchpaperverificationsystem.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScopusEntry {
    private String url;
    private String scopusID;
    private String title;
    private String creator;
    private String publicationName;
    private String issn;
    private ArrayList<Map<String,String>> isbn;
    private String volume;
    private String pageRanges;
    private String citedByCount;
    private ArrayList<Map<String,String>> affiliations ;
    private Map<String, Object> additionalDetails = new LinkedHashMap<>();

    @JsonProperty("prism:url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("prism:url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("dc:identifier")
    public String getScopusID() {
        return scopusID;
    }

    @JsonProperty("dc:identifier")
    public void setScopusID(String scopusID) {
        this.scopusID = scopusID;
    }

    @JsonProperty("dc:title")
    public String getTitle() {
        return title;
    }
    @JsonProperty("dc:title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("dc:creator")
    public String getCreator() {
        return creator;
    }

    @JsonProperty("dc:creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @JsonProperty("prism:publicationName")
    public String getPublicationName() {
        return publicationName;
    }

    @JsonProperty("prism:publicationName")
    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName;
    }

    @JsonProperty("prism:issn")
    public String getIssn() {
        return issn;
    }

    @JsonProperty("prism:issn")
    public void setIssn(String issn) {
        this.issn = issn;
    }

    @JsonProperty("prism:isbn")
    public ArrayList<Map<String,String>> getIsbn() {
        return isbn;
    }

    @JsonProperty("prism:isbn")
    public void setIsbn(ArrayList<Map<String,String>> isbn) {
        this.isbn = isbn;
    }

    @JsonProperty("prism:volume")
    public String getVolume() {
        return volume;
    }

    @JsonProperty("prism:volume")
    public void setVolume(String volume) {
        this.volume = volume;
    }

    @JsonProperty("prism:pageRange")
    public String getPageRanges() {
        return pageRanges;
    }

    @JsonProperty("prism:pageRange")
    public void setPageRanges(String pageRanges) {
        this.pageRanges = pageRanges;
    }

    @JsonProperty("citedby-count")
    public String getCitedByCount() {
        return citedByCount;
    }

    @JsonProperty("citedby-count")
    public void setCitedByCount(String citedByCount) {
        this.citedByCount = citedByCount;
    }

    @JsonProperty("affiliation")
    public ArrayList<Map<String, String>> getAffiliations() {
        return affiliations;
    }

    @JsonProperty("affiliation")
    public void setAffiliations(ArrayList<Map<String, String>> affiliations) {
        this.affiliations = affiliations;
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
        return "ScopusEntry [additionalDetails=" + additionalDetails + ", affiliations=" + affiliations
                + ", citedByCount=" + citedByCount + ", creator=" + creator + ", isbn=" + isbn + ", issn=" + issn
                + ", pageRanges=" + pageRanges + ", publicationName=" + publicationName + ", scopusID=" + scopusID
                + ", title=" + title + ", url=" + url + ", volume=" + volume + "]";
    }
    

}

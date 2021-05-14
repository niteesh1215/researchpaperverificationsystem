package com.kristujayanticollege.researchpaperverificationsystem.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScopusResponse {
    
    private int totalResults;
    private ArrayList<ScopusEntry> entries;
    private Map<String, Object> additionalDetails = new LinkedHashMap<>();

    @JsonProperty("opensearch:totalResults")
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    @JsonProperty("opensearch:totalResults")
    public int getTotalResults() {
        return this.totalResults;
    }

    @JsonProperty("entry")
    public ArrayList<ScopusEntry> getEntries() {
        return entries;
    }

    @JsonProperty("entry")
    public void setEntries(ArrayList<ScopusEntry> entries) {
        this.entries = entries;
    }

    @JsonAnySetter
    public void setAdditionalDetails(String key, Object value) {
        this.additionalDetails.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalDetails() {
        return this.additionalDetails;
    }

    @Override
    public String toString() {
        return "ScopusResponse [additionalDetails=" + additionalDetails + ", entries=" + entries + ", totalResults="
                + totalResults + "]";
    }
    
}

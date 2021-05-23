package com.kristujayanticollege.researchpaperverificationsystem.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kristujayanticollege.researchpaperverificationsystem.model.ResearchDetailsRow;
import com.kristujayanticollege.researchpaperverificationsystem.model.ScopusEntry;
import com.kristujayanticollege.researchpaperverificationsystem.model.ScopusResponse;
import com.kristujayanticollege.researchpaperverificationsystem.model.VerificationDetails;
import com.kristujayanticollege.researchpaperverificationsystem.projectenums.VerificationStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ScopusVerificationService {
    private WebClient.Builder webClientBuilder;
    private ResearchDetailsRow researchDetailsRow;
    private ObjectMapper objectMapper;

    private String apiKey;

    final String url = "https://api.elsevier.com/content/search/scopus?";

    double verificationScore = 0.0;
    int lastMatchFoundIndex = -1;

    boolean fullNameMatch = false;
    // boolean orcidMatch = false;
    boolean manuscriptTitleMatch = false;
    boolean journalTitleMatch = false;
    boolean volumeNumberMatch = false;
    boolean issnMatch = false;
    // boolean isAffiliatedToKJCMatch = false;

    ScopusEntry matchedEntry = null;

    public ScopusVerificationService() {

    }

    public ScopusVerificationService(String apiKey, WebClient.Builder webClientBuilder, ObjectMapper objectMapper,
            ResearchDetailsRow researchDetailsRow) {
        this.apiKey = apiKey;
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
        this.researchDetailsRow = researchDetailsRow;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setResearchDetailsRow(ResearchDetailsRow researchDetailsRow) {
        this.researchDetailsRow = researchDetailsRow;
    }

    public void setWebClientBuilder(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ScopusEntry getMatchedEntry() {
        return this.matchedEntry;
    }

    public ScopusResponse retriveFromRepository(String encodedQuery) {
        try {

            /*
             * WebClient.builder() .build() .uri(URI.create(
             * "https://RABBIT_HOSTNAME/api/queues/%2F/QUEUE_NAME")) .get().exchange();
             */

            Map response = webClientBuilder.build().get().uri(URI.create(this.url + encodedQuery))
                    .header("accept", "application/json").header("X-ELS-APIKey", this.apiKey).retrieve()
                    .bodyToMono(Map.class).block();

            ScopusResponse scopusResponse = objectMapper.convertValue(response.get("search-results"),
                    ScopusResponse.class);

            //System.out.println(scopusResponse);

            return scopusResponse;
        } catch (Exception e) {
            //System.out.println(e);
            return null;
        }

    }

    public void verify() {

        //resetting the value for next verification check
        verificationScore = 0.0;
        fullNameMatch = false;
        // orcidMatch = false;
        manuscriptTitleMatch = false;
        journalTitleMatch = false;
        volumeNumberMatch = false;
        issnMatch = false;
        lastMatchFoundIndex = -1;
        matchedEntry = null;

        if (this.researchDetailsRow == null)
            throw new IllegalStateException(
                    "researchDetailsRow cannot be null, must call setResearchDetailsRow setter before verify method");
        else if (this.webClientBuilder == null)
            throw new IllegalStateException(
                    "webClientBuilder cannot be null, set the webClientBuilder using setWebClientBuilder setter");
        else if (this.apiKey == null)
            throw new IllegalStateException("apiKey cannot be null, set the apiKey using setApiKey setter");
        else if (this.objectMapper == null)
            throw new IllegalStateException(
                    "objectMapper cannot be null, set the objectMapper using setObjectMapper setter");

        int start = 0;
        int totalIterations = 0;
        final int maxIterations = 20, count = 25;
        int totalResults = -1;

        String query = "title-abs-key(" + researchDetailsRow.getManuscriptTitle().trim().toLowerCase()
                .replaceAll("\\(", "").replaceAll("\\)", "") + ")";

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        do {

            ScopusResponse scopusResponse = retriveFromRepository(
                    "start=" + start + "&count=" + count + "&query=" + encodedQuery);

            if ((scopusResponse == null) || (scopusResponse.getEntries() == null))
                break;

            if (checkMatch(scopusResponse.getEntries()))
                break;

            if (totalResults == -1)
                totalResults = scopusResponse.getTotalResults();

            start += count;
            totalIterations++;

        } while (((totalResults - start) > 0) && (totalIterations <= maxIterations));

    }

    // returns true if all the field matches are found else false
    public boolean checkMatch(List<ScopusEntry> entries) {

        double fullNameMatchScore = 0;
        double manuscriptTitleMatchScore = 0;
        double journalTitleMatchScore = 0;

        boolean fullNameMatch = false;
        // boolean orcidMatch = false;
        boolean manuscriptTitleMatch = false;
        boolean journalTitleMatch = false;
        boolean volumeNumberMatch = false;
        boolean issnMatch = false;

        boolean completeMatchFlag = false;

        for (var i = 0; i < entries.size(); i++) {

            try {
                ScopusEntry entry = entries.get(i);
                // checking if the author name match is greater than 60 percent
                if (entry.getCreator() != null) {
                    fullNameMatchScore = diceCoefficientOptimized(entry.getCreator().toLowerCase(),
                            researchDetailsRow.getFullName().toLowerCase());
                    if (fullNameMatchScore > 0.6) {
                        fullNameMatch = true;
                    }
                }

                // checking if the manuscript title match is greater than 60 percent
                if (entry.getTitle() != null) {
                    manuscriptTitleMatchScore = diceCoefficientOptimized(entry.getTitle().toLowerCase(),
                            researchDetailsRow.getManuscriptTitle().toLowerCase());
                    if (manuscriptTitleMatchScore > 0.6) {
                        manuscriptTitleMatch = true;
                    }
                }
                // checking if the journal name match is greater than 60 percent
                if (entry.getPublicationName() != null) {
                    journalTitleMatchScore = diceCoefficientOptimized(entry.getPublicationName().toLowerCase(),
                            researchDetailsRow.getJournalTitle().toLowerCase());
                    if (journalTitleMatchScore > 0.6) {
                        journalTitleMatch = true;
                    }
                }

                if (entry.getVolume() != null)
                    if (researchDetailsRow.getVolumeNumber().trim().equalsIgnoreCase(entry.getVolume())) {
                        volumeNumberMatch = true;
                    }

                if (entry.getIssn() != null)
                    if (researchDetailsRow.getIssn().trim().equalsIgnoreCase(entry.getIssn())) {
                        issnMatch = true;
                    }

            } catch (Exception e) {

                //System.out.println(e);

            }

            if (fullNameMatch && journalTitleMatch && manuscriptTitleMatch) {
                double totalMatchScore = fullNameMatchScore + manuscriptTitleMatchScore + journalTitleMatchScore;

                if (volumeNumberMatch) {
                    totalMatchScore += 1.0;
                }

                if (issnMatch) {
                    totalMatchScore += 1.0;
                }

                if (totalMatchScore > verificationScore) {
                    lastMatchFoundIndex = i;
                    verificationScore = totalMatchScore;
                    matchedEntry = entries.get(i);

                    this.manuscriptTitleMatch = manuscriptTitleMatch;
                    this.journalTitleMatch = journalTitleMatch;
                    this.fullNameMatch = fullNameMatch;
                    this.issnMatch = issnMatch;
                    this.volumeNumberMatch = volumeNumberMatch;
                }

                if (volumeNumberMatch && issnMatch) {
                    completeMatchFlag = true;
                    break;
                }
            }
        }
        return completeMatchFlag;
    }

    @Transactional
    public void saveStatus(VerificationDetailsRepositoryService verificationDetailsRepositoryService,
            ResearchDetailsRepositoryService researchDetailsRepositoryService) {
        VerificationStatus status;
        VerificationDetails verificationDetails = new VerificationDetails();
        verificationDetails.setResearchDetailsRowId(researchDetailsRow.getId());

        if (lastMatchFoundIndex != -1) {
            verificationDetails.setFullNameMatch(fullNameMatch);
            verificationDetails.setFetchedFullName(matchedEntry.getCreator());

            verificationDetails.setManuscriptTitleMatch(manuscriptTitleMatch);
            verificationDetails.setFetchedManuscriptTitle(matchedEntry.getTitle());

            verificationDetails.setJournalTitleMatch(journalTitleMatch);
            verificationDetails.setFetchedJournalTitle(matchedEntry.getPublicationName());

            verificationDetails.setVolumeNumberMatch(volumeNumberMatch);
            verificationDetails.setFetchedVolumeNumber(matchedEntry.getVolume());

            verificationDetails.setIssnMatch(issnMatch);
            verificationDetails.setFetchedIssn(matchedEntry.getIssn());

            verificationDetails.setVerificationMatchPercentage(verificationScore / 5); // divide by 5 because checked
                                                                                       // for 5 fields

            if (fullNameMatch && manuscriptTitleMatch && journalTitleMatch && volumeNumberMatch) {
                status = VerificationStatus.FOUND;
            } else {
                status = VerificationStatus.PARTIAL_MATCH;
            }

        } else {
            status = VerificationStatus.NOT_FOUND;
        }

        verificationDetailsRepositoryService.save(verificationDetails);
        researchDetailsRepositoryService.updateVerificationStatus(researchDetailsRow.getId(), status);

    }

    public static double diceCoefficientOptimized(String s, String t) {
        // Verifying the input:
        if (s == null || t == null)
            return 0;
        // Quick check to catch identical objects:
        if (s == t)
            return 1;
        // avoid exception for single character searches
        if (s.length() < 2 || t.length() < 2)
            return 0;

        // Create the bigrams for string s:
        final int n = s.length() - 1;
        final int[] sPairs = new int[n];
        for (int i = 0; i <= n; i++)
            if (i == 0)
                sPairs[i] = s.charAt(i) << 16;
            else if (i == n)
                sPairs[i - 1] |= s.charAt(i);
            else
                sPairs[i] = (sPairs[i - 1] |= s.charAt(i)) << 16;

        // Create the bigrams for string t:
        final int m = t.length() - 1;
        final int[] tPairs = new int[m];
        for (int i = 0; i <= m; i++)
            if (i == 0)
                tPairs[i] = t.charAt(i) << 16;
            else if (i == m)
                tPairs[i - 1] |= t.charAt(i);
            else
                tPairs[i] = (tPairs[i - 1] |= t.charAt(i)) << 16;

        // Sort the bigram lists:
        Arrays.sort(sPairs);
        Arrays.sort(tPairs);

        // Count the matches:
        int matches = 0, i = 0, j = 0;
        while (i < n && j < m) {
            if (sPairs[i] == tPairs[j]) {
                matches += 2;
                i++;
                j++;
            } else if (sPairs[i] < tPairs[j])
                i++;
            else
                j++;
        }
        return (double) matches / (n + m);
    }

}

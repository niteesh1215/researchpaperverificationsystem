package com.kristujayanticollege.researchpaperverificationsystem.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.kristujayanticollege.researchpaperverificationsystem.model.ResearchDetailsRow;

public class PeerReviewedVerificationService {
    private final String url = "https://www.google.com/search?q=";

    WebClient webClient;

    private ResearchDetailsRow researchDetailsRow;

    List<String> foundUrls = new ArrayList<String>();

    WebPagePdfExtractor webPagePdfExtractor;

    double verificationScore = 0.0;

    boolean fullNameMatch = false;
    // boolean orcidMatch = false;
    boolean manuscriptTitleMatch = false;
    boolean journalTitleMatch = false;
    boolean volumeNumberMatch = false;
    boolean issnMatch = false;

    PeerReviewedVerificationService() {
        webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);

        webClient.getOptions().setRedirectEnabled(true);

        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getCookieManager().setCookiesEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setGeolocationEnabled(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setPopupBlockerEnabled(false);

        webClient.getOptions().setTimeout(15000);
        webClient.setJavaScriptTimeout(8000);

        webPagePdfExtractor = new WebPagePdfExtractor();
    }

    public void setResearchDetailsRow(ResearchDetailsRow researchDetailsRow) {
        this.researchDetailsRow = researchDetailsRow;
    }

    public void verify() {

        if (this.researchDetailsRow == null)
            throw new IllegalStateException(
                    "researchDetailsRow cannot be null, must call setResearchDetailsRow setter before verify method");

        // webClient.waitForBackgroundJavaScript(500);

        try {
            HtmlPage currentPage = webClient.getPage(url);

            @SuppressWarnings("unchecked")
            List<HtmlElement> elements = (List<HtmlElement>) (Object) currentPage.getByXPath(".//a[.//cite]");

            List<String> urlList = new ArrayList<String>();

            for (HtmlElement element : elements) {
                String newPageUrl = element.getAttribute("href");

                if (newPageUrl != null && !newPageUrl.contains("youtube.com"))
                    urlList.add(newPageUrl);
            }

            boolean javascriptEnabledFlag = true;

            for (int i = 0; i < urlList.size(); i++) {
                if (urlList.get(i).endsWith(".pdf")) {

                    try {
                        Map<String, Object> extractedMap = webPagePdfExtractor.processRecord(urlList.get(i));

                        if (extractedMap.get("text") != null) {

                            findMatches((String) extractedMap.get("text"));

                            /*
                             * if (((String) extractedMap.get("text")).toLowerCase()
                             * .contains("Indian Politics & Law Review Journal".toLowerCase())) {
                             * System.out.println(true); foundUrls.add(urlList.get(i)); }
                             */
                        }
                    } catch (Exception e) {

                    }

                } else {
                    try {

                        System.out.println(urlList.get(i));

                        HtmlPage page = webClient.getPage(urlList.get(i));

                        //System.out.println("hi" + i);

                        String pageText = page.asNormalizedText();
                        if (pageText != null) {

                            findMatches(pageText);

                            System.out.println(pageText.substring(0, 10));
                        }
                        if (!javascriptEnabledFlag)
                            webClient.getOptions().setJavaScriptEnabled(true);
                    } catch (FailingHttpStatusCodeException e) {

                    } catch (IOException e) {
                    } catch (Exception e) {
                        if (javascriptEnabledFlag) {
                            i--;
                            javascriptEnabledFlag = false;
                            webClient.getOptions().setJavaScriptEnabled(false);
                        } else {
                            javascriptEnabledFlag = true;
                            webClient.getOptions().setJavaScriptEnabled(true);
                        }

                    }
                }


                if(manuscriptTitleMatch && journalTitleMatch && fullNameMatch)
                    foundUrls.add(urlList.get(i));

                if (foundUrls.size() == 3)
                    break;
            }

        } catch (Exception e) {

        }
    }

    void findMatches(String scrapedText) {

        double verificationScore = 0.0;
        scrapedText = scrapedText.toLowerCase();

        // searching for manuscript title

        String manuscriptTitle = researchDetailsRow.getManuscriptTitle().toLowerCase();

        if (scrapedText.contains(manuscriptTitle)) {
            manuscriptTitleMatch = true;
        } else {

            // replacing "and" word with "&" and rechecking
            manuscriptTitle = manuscriptTitle.replaceAll("\\band\\b", "&");

            if (scrapedText.contains(manuscriptTitle))
                manuscriptTitleMatch = true;
        }

        if (manuscriptTitleMatch)
            verificationScore += 1;

        String journalTitle = researchDetailsRow.getJournalTitle().toLowerCase();

        if (scrapedText.contains(journalTitle)) {
            journalTitleMatch = true;
        } else {

            // replacing "and" word with "&" and rechecking
            journalTitle = journalTitle.replaceAll("\\band\\b", "&");

            if (scrapedText.contains(journalTitle))
                journalTitleMatch = true;
        }

        if (journalTitleMatch)
            verificationScore += 1;

        String authorName = researchDetailsRow.getFullName().toLowerCase();

        if (scrapedText.contains(authorName)) {
            verificationScore += 1;
        } else {
            String[] authorNameSliced = authorName.split(" ");

            int i = 0;
            for (String part : authorNameSliced) {

                if (scrapedText.contains(part))
                    i++;
            }

            verificationScore += i == authorNameSliced.length ? 1 : i / authorNameSliced.length;
        }

        if (scrapedText.contains(researchDetailsRow.getIssn()))
            verificationScore += 1;

        if (scrapedText.contains(researchDetailsRow.getVolumeNumber()))
            verificationScore += 1;

        if (this.verificationScore < verificationScore)
            this.verificationScore = verificationScore;
    }
}
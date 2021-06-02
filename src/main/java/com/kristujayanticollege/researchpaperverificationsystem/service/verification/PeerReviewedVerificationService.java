package com.kristujayanticollege.researchpaperverificationsystem.service.verification;

import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserListener;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;
import com.kristujayanticollege.researchpaperverificationsystem.model.ResearchDetailsRow;
import com.kristujayanticollege.researchpaperverificationsystem.model.VerificationDetails;
import com.kristujayanticollege.researchpaperverificationsystem.projectenums.VerificationStatus;
import com.kristujayanticollege.researchpaperverificationsystem.service.other.WebPagePdfExtractor;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.ResearchDetailsRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.VerificationDetailsRepositoryService;

import org.apache.commons.lang3.StringUtils;

public class PeerReviewedVerificationService {

    private final String url = "https://www.google.com/search?q=";

    WebClient webClient;

    private ResearchDetailsRow researchDetailsRow;

    public List<String> foundUrls = new ArrayList<String>();

    WebPagePdfExtractor webPagePdfExtractor;

    double verificationScore = 0.0;

    boolean fullNameMatch = false;
    // boolean orcidMatch = false;
    boolean manuscriptTitleMatch = false;
    boolean journalTitleMatch = false;
    boolean volumeNumberMatch = false;
    boolean issnMatch = false;

    public PeerReviewedVerificationService() {

        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "fatal");

        webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(false);

        webClient.getOptions().setRedirectEnabled(true);

        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getCookieManager().setCookiesEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setGeolocationEnabled(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setPopupBlockerEnabled(false);

        webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
        webClient.setCssErrorHandler(new SilentCssErrorHandler());

        webClient.setIncorrectnessListener(new IncorrectnessListener() {

            @Override
            public void notify(String arg0, Object arg1) {
                // TODO Auto-generated method stub

            }
        });

        webClient.setHTMLParserListener(new HTMLParserListener() {

            @Override
            public void error(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {
                // TODO Auto-generated method stub

            }

            @Override
            public void warning(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {
                // TODO Auto-generated method stub

            }

        });

        webClient.getOptions().setTimeout(15000);
        webClient.setJavaScriptTimeout(8000);
        webClient.waitForBackgroundJavaScript(800);

        webPagePdfExtractor = new WebPagePdfExtractor();
    }

    public void setResearchDetailsRow(ResearchDetailsRow researchDetailsRow) {
        this.researchDetailsRow = researchDetailsRow;
    }

    public void verify() {

        verificationScore = 0.0;

        this.manuscriptTitleMatch = false;
        this.journalTitleMatch = false;
        this.fullNameMatch = false;
        this.issnMatch = false;
        this.volumeNumberMatch = false;

        foundUrls = new ArrayList<String>();

        if (this.researchDetailsRow == null)
            throw new IllegalStateException(
                    "researchDetailsRow cannot be null, must call setResearchDetailsRow setter before verify method");

        // webClient.waitForBackgroundJavaScript(500);

        try {

            String encodedQuery = URLEncoder.encode(
                    researchDetailsRow.getManuscriptTitle() + " " + researchDetailsRow.getFullName(),
                    StandardCharsets.UTF_8);
            HtmlPage currentPage = webClient.getPage(url + encodedQuery);

            @SuppressWarnings("unchecked")
            List<HtmlElement> elements = (List<HtmlElement>) (Object) currentPage.getByXPath(".//a[.//cite]");

            List<String> urlList = new ArrayList<String>();

            for (HtmlElement element : elements) {
                String newPageUrl = element.getAttribute("href");

                if (newPageUrl != null && !newPageUrl.contains("youtube.com"))
                    urlList.add(newPageUrl);
            }

            System.out.println("********************************");
            System.out.println(urlList);
            System.out.println("********************************");
            boolean javascriptEnabledFlag = true;

            String contentType = null;

            for (int i = 0; i < urlList.size(); i++) {
                if (contentType == null) {
                    try {
                        contentType = webClient.getPage(urlList.get(i)).getWebResponse().getContentType();
                    } catch (UnknownHostException e) {
                        continue;
                    } catch (Exception e) {
                        contentType = null;
                    }
                }
                if (urlList.get(i).endsWith(".pdf") || (contentType != null && contentType.contains("pdf"))) {

                    try {
                        Map<String, Object> extractedMap = webPagePdfExtractor.processRecord(urlList.get(i));

                        if (extractedMap.get("text") != null) {

                            if (findMatches((String) extractedMap.get("text")))
                                foundUrls.add(urlList.get(i));

                        }
                    } catch (Exception e) {

                    }

                } else {
                    try {

                        System.out.println(urlList.get(i));

                        HtmlPage page = webClient.getPage(urlList.get(i));

                        // System.out.println("hi" + i);

                        String pageText = page.asNormalizedText();
                        if (pageText != null) {

                            if (findMatches(pageText)) {
                                foundUrls.add(urlList.get(i));
                            }

                        }
                        if (!javascriptEnabledFlag)
                            webClient.getOptions().setJavaScriptEnabled(true);
                    } catch (FailingHttpStatusCodeException e) {

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

                contentType = null;

                if (foundUrls.size() == 2)
                    break;
            }

        } catch (Exception e) {
            System.out.println("#####################");
        }
    }

    // returns true if match found else false
    boolean findMatches(String scrapedText) {

        double verificationScore = 0.0;

        boolean fullNameMatch = false;
        // boolean orcidMatch = false;
        boolean manuscriptTitleMatch = false;
        boolean journalTitleMatch = false;
        boolean volumeNumberMatch = false;
        boolean issnMatch = false;

        scrapedText = scrapedText.toLowerCase();
        scrapedText = StringUtils.normalizeSpace(scrapedText);

        // System.out.println(scrapedText);

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

            fullNameMatch = true;
        } else {
            String[] authorNameSliced = authorName.split(" ");

            int i = 0;
            for (String part : authorNameSliced) {

                if (scrapedText.contains(part))
                    i++;
            }

            if (i == authorNameSliced.length) {
                fullNameMatch = true;
                verificationScore += 1;
            } else {
                verificationScore += (i * 1.0) / authorNameSliced.length;
            }
        }

        if (scrapedText.contains(researchDetailsRow.getIssn())) {
            verificationScore += 1;
            issnMatch = true;

        } else if (scrapedText.contains(researchDetailsRow.getIssn().replace("-", " "))) {
            verificationScore += 1;
            issnMatch = true;
        } else if (scrapedText.contains(researchDetailsRow.getIssn().replace(" ", ""))) {
            verificationScore += 1;
            issnMatch = true;
        }

        if (scrapedText.contains(researchDetailsRow.getVolumeNumber())) {
            verificationScore += 1;
            volumeNumberMatch = true;
        }

        if (this.verificationScore < verificationScore) {
            this.verificationScore = verificationScore;

            this.manuscriptTitleMatch = manuscriptTitleMatch;
            this.journalTitleMatch = journalTitleMatch;
            this.fullNameMatch = fullNameMatch;
            this.issnMatch = issnMatch;
            this.volumeNumberMatch = volumeNumberMatch;
        }

        if (manuscriptTitleMatch && journalTitleMatch && fullNameMatch)
            return true;
        else
            return false;

    }

    @Transactional
    public void saveStatus(VerificationDetailsRepositoryService verificationDetailsRepositoryService,
            ResearchDetailsRepositoryService researchDetailsRepositoryService) {
        VerificationStatus status;
        VerificationDetails verificationDetails = new VerificationDetails();
        verificationDetails.setResearchDetailsRowId(researchDetailsRow.getId());

        if (foundUrls.size() != 0) {
            verificationDetails.setFullNameMatch(fullNameMatch);
            verificationDetails.setManuscriptTitleMatch(manuscriptTitleMatch);
            verificationDetails.setJournalTitleMatch(journalTitleMatch);
            verificationDetails.setVolumeNumberMatch(volumeNumberMatch);
            verificationDetails.setIssnMatch(issnMatch);

            // divide by 5 because checked for 5 fields
            verificationDetails.setVerificationMatchPercentage(verificationScore / 5);
            verificationDetails.setFoundAtUrls(foundUrls);

            if (fullNameMatch && manuscriptTitleMatch && journalTitleMatch) {
                status = VerificationStatus.FOUND;
            } else {
                status = VerificationStatus.NOT_FOUND;
            }

        } else {
            status = VerificationStatus.NOT_FOUND;
        }

        verificationDetailsRepositoryService.save(verificationDetails);
        researchDetailsRepositoryService.updateVerificationStatus(researchDetailsRow.getId(), status);
    }
}
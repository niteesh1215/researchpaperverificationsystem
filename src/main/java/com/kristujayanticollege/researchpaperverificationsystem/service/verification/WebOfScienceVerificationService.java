package com.kristujayanticollege.researchpaperverificationsystem.service.verification;

import java.net.URL;
import java.util.ArrayList;

import javax.transaction.Transactional;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserListener;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;
import com.kristujayanticollege.researchpaperverificationsystem.model.ResearchDetailsRow;
import com.kristujayanticollege.researchpaperverificationsystem.model.VerificationDetails;
import com.kristujayanticollege.researchpaperverificationsystem.projectenums.VerificationStatus;
import com.kristujayanticollege.researchpaperverificationsystem.service.other.WebPagePdfExtractor;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.ResearchDetailsRepositoryService;
import com.kristujayanticollege.researchpaperverificationsystem.service.repository.VerificationDetailsRepositoryService;

import org.apache.commons.lang3.StringUtils;

public class WebOfScienceVerificationService {
    private final String url = "https://mjl.clarivate.com/search-results";

    WebClient webClient;

    private ResearchDetailsRow researchDetailsRow;

    boolean journalFound = false;

    WebPagePdfExtractor webPagePdfExtractor;

    double verificationScore = 0.0;

    boolean fullNameMatch = false;
    // boolean orcidMatch = false;
    boolean manuscriptTitleMatch = false;
    boolean journalTitleMatch = false;
    boolean volumeNumberMatch = false;
    boolean issnMatch = false;

    public WebOfScienceVerificationService() {
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "fatal");

        webClient = new WebClient(BrowserVersion.FIREFOX_78);

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

            }
        });

        webClient.setHTMLParserListener(new HTMLParserListener() {

            @Override
            public void error(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {

            }

            @Override
            public void warning(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {

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
        if (this.researchDetailsRow == null)
            throw new IllegalStateException(
                    "researchDetailsRow cannot be null, must call setResearchDetailsRow setter before verify method");

        try {

            HtmlPage currentPage = webClient.getPage(url);

            final HtmlTextInput searchField = currentPage.getHtmlElementById("search-box");
            final HtmlButton searchButton = currentPage.getHtmlElementById("search-button");

            String issn = researchDetailsRow.getIssn().replaceAll("[^0-9]", "").trim();
            printAstriex();
            System.out.println(issn);
            printAstriex();

            searchField.type(issn);

            final HtmlPage page2 = searchButton.click();

            int index = page2.asNormalizedText().indexOf("Found");
            int tries = 5;
            while (tries > 0 && index == -1) { // you can change number of tries and condition according to your
                                               // need
                tries--;
                synchronized (page2) {
                    page2.wait(2000); // wait
                }
                index = page2.asNormalizedText().indexOf("Found");// input length is example of condtion
            }

            printAstriex();
            System.out.println(index);
            printAstriex();
            String text = page2.asNormalizedText();
            text = text.toLowerCase();
            text = StringUtils.normalizeSpace(text);

            text = text.replaceAll("[^A-Za-z0-9\\s]", "");

            if (text.contains("eissn " + issn)) {

                journalFound = true;

                printAstriex();
                print("Match Found");
                printAstriex();

                boolean javascriptEnabledFlag = true;

                while (true) {
                    try {

                        System.out.println(researchDetailsRow.getDoiUrlLink());

                        HtmlPage page = webClient.getPage(researchDetailsRow.getDoiUrlLink());

                        // System.out.println("hi" + i);

                        String pageText = page.asNormalizedText();
                        if (pageText != null) {

                            findMatches(pageText);
                            break;

                        }

                    } catch (FailingHttpStatusCodeException e) {

                    } catch (Exception e) {
                        if (javascriptEnabledFlag) {
                            javascriptEnabledFlag = false;
                            webClient.getOptions().setJavaScriptEnabled(false);
                        } else {
                            break;
                        }

                    }

                    if (!javascriptEnabledFlag) {
                        break;
                    }
                }

            }

            // System.out.println(text.length() + researchDetailsRow.getIssn()
            // + text.indexOf(researchDetailsRow.getIssn().trim().toLowerCase()));
            // System.out.println(text.contains(researchDetailsRow.getJournalTitle().toLowerCase()));
            // printAstriex();
            // System.out.println(text);
            // printAstriex();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    boolean findMatches(String scrapedText) {

        print(scrapedText);

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
        } else {
            String issn = researchDetailsRow.getIssn().replaceAll("[^0-9]", "");

            boolean s1 = scrapedText.contains(issn.substring(0, 3));
            boolean s2 = scrapedText.contains(issn.substring(3));

            if (s1 && s2) {
                verificationScore += 1;
                issnMatch = true;
            }

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

        verificationDetails.setFullNameMatch(fullNameMatch);
        verificationDetails.setManuscriptTitleMatch(manuscriptTitleMatch);
        verificationDetails.setJournalTitleMatch(journalTitleMatch);
        verificationDetails.setVolumeNumberMatch(volumeNumberMatch);
        verificationDetails.setIssnMatch(journalFound);

        // divide by 5 because checked for 5 fields
        verificationDetails.setVerificationMatchPercentage(verificationScore / 5);
        ArrayList<String> a = new ArrayList<String>();
        a.add(researchDetailsRow.getDoiUrlLink());

        verificationDetails.setFoundAtUrls(a);

        if (fullNameMatch && manuscriptTitleMatch && journalTitleMatch) {
            status = VerificationStatus.FOUND;
        } else {
            status = VerificationStatus.NOT_FOUND;
        }

        verificationDetailsRepositoryService.save(verificationDetails);
        researchDetailsRepositoryService.updateVerificationStatus(researchDetailsRow.getId(), status);
    }

    void printAstriex() {
        System.out.println("================================");
    }

    void print(String s) {
        System.out.println(s);
    }
}

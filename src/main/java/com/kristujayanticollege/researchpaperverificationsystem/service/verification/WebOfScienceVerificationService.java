package com.kristujayanticollege.researchpaperverificationsystem.service.verification;

import java.net.URL;

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

public class WebOfScienceVerificationService {
    private final String url = "https://mjl.clarivate.com/search-results";

    WebClient webClient;

    private ResearchDetailsRow researchDetailsRow;

    boolean journalFound = false;

    public WebOfScienceVerificationService() {
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

            String issn = researchDetailsRow.getIssn().replaceAll("-", "").replaceAll(" ", "").trim();

            searchField.type(issn);

            final HtmlPage page2 = searchButton.click();

            System.out.println(page2.asNormalizedText().indexOf(researchDetailsRow.getIssn().trim(), 0));

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

package com.albersheim.webcrawler.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class WebCrawlerServiceTest {
    private WebCrawlerService webCrawlerService;

    @Before
    public void setUp() throws Exception {
        webCrawlerService = new WebCrawlerService();
    }

    @After
    public void tearDown() throws Exception {
    }

    // 1st test - get non-zero length content, given a url
    @Test
    public void testGivenUrl_GetPageContent_returnsNonZeroLengthContent() {
        String url = "http://www.google.com";
        String html = webCrawlerService.getPageContent(url);
        assertNotNull(html);
        assertNotEquals(0,html.length());
    }

    // 2nd test - forgot protocol in url - negative case
    @Test
    public void testGivenUrl_checkBadUrl_returnsFalse() {
        String url = "google.com";
        Boolean ret = webCrawlerService.checkUrlIsValidFormat(url);
        assertFalse(ret);
    }

    // 3rd test - add protocol in url - positive case
    @Test
    public void testGivenUrl_checkGoodUrl_returnsTrue() {
        String url = "http://www.google.com";
        Boolean ret = webCrawlerService.checkUrlIsValidFormat(url);
        assertTrue(ret);
    }

    // 4th test - get actual content, given a url
    @Test
    public void testGivenUrl_GetPageContent_returnsContent() {
        String url = "http://www.google.com";
        String html = webCrawlerService.getPageContent(url);
        assertTrue(html.toLowerCase().contains("google"));
    }

    // 5th test - get list of pages from given page, given initial page content
    @Test
    public void testGivenUrl_GetListOfPagesFromInitial_returnsList() {
        String url = "https://wiprodigital.com";
        String html = webCrawlerService.getPageContent(url);
        List<String> pages = webCrawlerService.getPagesInPage(html,url);
        assertNotNull(pages);
        assertNotEquals(0,pages.size());
        assertTrue(pages.contains("https://wiprodigital.com/designit-approach/"));
    }

    @Test
    public void testGetPageFromHrefContent_returns_pageContent() {
        String hrefContent = "<a class=\"circles-di-link\" target=\"_blank\" href=\"https://wiprodigital.com/designit-approach/\">Designit</a>";
        String expected = "https://wiprodigital.com/designit-approach/";
        String actual = webCrawlerService.getPageFromHrefContent(hrefContent);
        assertEquals(expected,actual);
    }

}
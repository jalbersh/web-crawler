package com.albersheim.webcrawler.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Set;
import java.util.Vector;

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
        Set<String> pages = webCrawlerService.getPagesFromUrl(url);
        assertNotNull(pages);
        assertNotEquals(0,pages.size());
        assertFalse(pages.contains("https://wiprodigital.com/designit-approach/"));
    }

    @Test
    public void testGetPageFromHrefContent_returns_pageContent() {
        String hrefContent = "<a class=\"circles-di-link\" target=\"_blank\" href=\"https://wiprodigital.com/designit-approach/\">Designit</a>";
        String expected = "https://wiprodigital.com/designit-approach";
        HtmlPageExtractor htmlPageExtractor = new HtmlPageExtractor();
        Vector<HtmlPage> link = htmlPageExtractor.grabHtmlPages(hrefContent);
        assertEquals(1,link.size());
        String actual = link.get(0).getPage();
        assertEquals(expected,actual);
    }

    @Test
    public void testPageListShouldHaveNoRepeats() {
        String url = "https://wiprodigital.com";
        Set<String> pages = webCrawlerService.getPagesFromUrl(url);
        int occurrences = Collections.frequency(pages, "https://wiprodigital.com/who-we-are");
        assertEquals(1,occurrences);// red test failed
    }

    @Test
    public void testRecursiveCallMadeOnSubPageIncreasesPageCount() {
        String url = "https://wiprodigital.com";
        Set<String> pages = webCrawlerService.getPagesFromUrl(url);
        int count = pages.size();
//        assertEquals(47,count); // 47 pages
//        assertEquals(24,count); // 24 strips off after #
//        assertEquals(23,count); // 21 strips off if ending in /
//        assertEquals(14,count); // 14 pages strips off target
//        assertEquals(13,count); // 14 pages strips off title
//        assertEquals(11,count); // 11 pages strips off \"
        assertEquals(8,count); // 8 pages strips off /
        assertFalse(pages.contains("https://wiprodigital.com/who-we-are/"));
        assertFalse(pages.contains("https://wiprodigital.com/join-our-team\" target=\"_self\" title=\"better together"));
        assertFalse(pages.contains("https://wiprodigital.com/privacy-policy/\\\" title=\\\"Privacy policy"));
    }

    private boolean contains(Set<String> pages, String url) {
        for (String page : pages) {
            if (page.contains(url)) return true;
        }
        return false;
    }

    @Test
    public void testPageListDoesNotHaveOtherDomains() {
        String url = "https://www.google.com";
        Set<String> pages = webCrawlerService.getPagesFromUrl(url);
        assertFalse(contains(pages,"www.youtube.com"));// red test failed
    }

}
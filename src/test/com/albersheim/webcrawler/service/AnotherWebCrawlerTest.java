package com.albersheim.webcrawler.service;

import com.albersheim.webcrawler.controller.WebCrawlerController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AnotherWebCrawlerTest {

    private WebCrawlerController webCrawlerController;

    @Mock
    public volatile WebCrawlerService webCrawlerService;

    @Before
    public void setup() {
    }

    // Need test for subPages
//    @Test
    public void testSubPages_returnsPagesinSubPages() throws Exception {
        WebCrawlerService mockService = mock(WebCrawlerService.class);
        webCrawlerController = new WebCrawlerController((mockService));
        String html1 = "What we do <a href=\"https://wiprodigital.com/what-we-do\" title=\"What we do\">What we do</a>";
        String html2 = "We do a lot <a href=\"https://wiprodigital.com/what-we-do/ee-do-a-lot\" title=\"we do a lot\">We do a lot</a>";
        Mockito.doReturn(html1).when(mockService).getPageContent("https://wiprodigital.com");
        Mockito.doReturn(html2).when(mockService).getPageContent("https://wiprodigital.com/what-we-do");
        String url = "https://wiprodigital.com";
        Set<String> pages = webCrawlerController.getSiteMap(url);
        int count = pages.size();
        assertEquals(3,count);
        assertTrue(pages.contains("https://wiprodigital.com"));
        assertTrue(pages.contains("https://wiprodigital.com/what-we-do"));
        assertTrue(pages.contains("https://wiprodigital.com/what-we-do/ee-do-a-lot"));
    }
}

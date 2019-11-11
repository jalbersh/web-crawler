package com.albersheim.webcrawler.service;

import java.util.Vector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HtmlPageExtractorTest {

    private HtmlPageExtractor htmlPageExtractor;
    private String[] contents;
    String URL = "https://wiprodigital.com";

    @Before
    public void initData() {
        htmlPageExtractor = new HtmlPageExtractor();
        contents = HtmlContentProvider();
    }

    private String[] HtmlContentProvider() {
        return new String[] {
                new String ("abc hahaha <a href='" + URL + "'>wiprodigital</a>") ,
                new String ( "abc hahaha <a HREF='" + URL + "'>wiprodigital</a>" ),
                new String ( "abc hahaha <A HREF='" + URL + "'>wiprodigital</A> , "
                        + "abc hahaha <A HREF='" + URL + "' target='_blank'>wiprodigital</A>" ),
                new String ( "abc hahaha <A HREF='" + URL + "' target='_blank'>wiprodigital</A>" ),
                new String ( "abc hahaha <A target='_blank' HREF='" + URL + "'>wiprodigital</A>" ),
                new String ( "abc hahaha <A HREF=" + URL + " target='_blank' \">wiprodigital</A>" ),
                new String ( "abc hahaha <a HREF=" + URL + ">wiprodigital</a>" ),
                new String ("abc hahaha <a href='" + URL + "'>wiprodigital</a>"),
                new String ( "abc hahaha <a href='" + URL + "'>wiprodigital</a>" ),
                new String ( "abc hahaha <A href='" + URL + "'>wiprodigital</A> , "
                        + "abc hahaha <A HREF='" + URL + "' target='_blank'>google</A>" ),
                new String ( "abc hahaha <A href='" + URL + "' target='_blank'>wiprodigital</A>" ),
                new String ( "abc hahaha <A target='_blank' HREF='" + URL + "'>wiprodigital</A>" ),
                new String ( "abc hahaha <A href=" + URL + " target='_blank' \">wiprodigital</A>" ),
                new String ( "abc hahaha <a href=" + URL + ">wiprodigital</a>" )};
    }

    @Test
    public void testValidHtml_returnsPagesCorrectly() {
        for (String html : contents) {
            Vector<HtmlPage> pages = htmlPageExtractor.grabHtmlPages(html);
            //there must have something
            assertTrue(pages.size() != 0);
            for (int i = 0; i < pages.size(); i++) {
                HtmlPage htmlPages = pages.get(i);
                assertEquals(htmlPages.getPage(), URL);
            }
        }
    }

    @Test
    public void testAnotherValidHtml_returnsCorrectPage() {
        String html = "Privacy Policy <a href=\"https://wiprodigital.com/privacy-policy/\" title=\"Privacy Policy\">Privacy policy</a>";
        Vector<HtmlPage> link = htmlPageExtractor.grabHtmlPages(html);
        assertEquals(1,link.size());
        assertEquals("https://wiprodigital.com/privacy-policy",link.get(0).getPage());
    }

    @Test
    public void testValidHtmlWithHash_returnsPageWithoutHash() {
        String html = "Privacy Policy <a href=\"https://wiprodigital.com/what-we-do#work-three-circles-row\" title=\"Privacy Policy\">Privacy policy</a>";
        Vector<HtmlPage> link = htmlPageExtractor.grabHtmlPages(html);
        assertEquals(1,link.size());
        assertEquals("https://wiprodigital.com/what-we-do",link.get(0).getPage());
    }

    @Test
    public void testValidHtmlWithQuestionMark_returnsPageWithoutQuestionMark() {
        String html = "Privacy Policy <a href=\"https://wiprodigital.com/?s=&post_type[]=cases\" title=\"Privacy Policy\">Privacy policy</a>";
        Vector<HtmlPage> link = htmlPageExtractor.grabHtmlPages(html);
        assertEquals(1,link.size());
        assertEquals("https://wiprodigital.com",link.get(0).getPage());
    }

    @Test
    public void testValidHtmlEndingWithSlash_returnsPageWithoutEndingSlash() {
        String html = "Privacy Policy <a href=\"https://wiprodigital.com/\" title=\"Privacy Policy\">Privacy policy</a>";
        Vector<HtmlPage> link = htmlPageExtractor.grabHtmlPages(html);
        assertEquals(1,link.size());
        assertEquals("https://wiprodigital.com",link.get(0).getPage());
    }
}

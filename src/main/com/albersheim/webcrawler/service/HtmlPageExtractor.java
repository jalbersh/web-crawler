package com.albersheim.webcrawler.service;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlPageExtractor {
    private Pattern patternTag, patternLink;
    private Matcher matcherTag, matcherLink;
    private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    private static final String HTML_A_HREF_TAG_PATTERN =
            "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

    public HtmlPageExtractor() {
        patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
        patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
    }

    public Vector<HtmlPage> grabHtmlPages(final String html) {
        Vector<HtmlPage> result = new Vector<HtmlPage>();
        matcherTag = patternTag.matcher(html);
        while (matcherTag.find()) {
            String href = matcherTag.group(1); // href
            String pageText = matcherTag.group(2); // link text
            matcherLink = patternLink.matcher(href);
            while (matcherLink.find()) {
                String page = matcherLink.group(1); // link
                HtmlPage obj = new HtmlPage();
                if (page.contains("#")) {
                    int hash = page.indexOf("#");
                    if (hash > 0) page = page.substring(0,hash);
                }
                if (page.contains("?")) {
                    int hash = page.indexOf("?");
                    if (hash > 0) page = page.substring(0,hash);
                }
                int slash = page.substring(page.length()-3).indexOf("/");
                if (slash > -1) {
                    page = page.substring(0,page.length()-3+slash);
                }
                obj.setPage(page);
                obj.setPageText(pageText);
//                System.out.println("page="+page);
                result.add(obj);
            }
        }
        return result;
    }
}


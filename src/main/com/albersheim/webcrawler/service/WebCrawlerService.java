package com.albersheim.webcrawler.service;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

@Service
public class WebCrawlerService {

    public String getPageContent(String url) {
//        System.out.println("url="+url);
        if (!checkUrlIsValidFormat(url)) {
            System.out.println(url+" is not valid");
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            URL urlObj = new URL(url);
            URLConnection urlCon = urlObj.openConnection();

            InputStream inputStream = urlCon.getInputStream();
            BufferedInputStream reader = new BufferedInputStream(inputStream);

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = reader.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, bytesRead));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Exception caught: "+e.getMessage());
        }
//        System.out.println("html="+sb.toString());
        return sb.toString(); // after getting content
//        return null; // initial value for red test
    }

    public Boolean checkUrlIsValidFormat(String url) {
        if (url.length() < 8) return false; // "http://
        if (!url.contains("http") && !url.contains("https")) return false;
        return true;
//        return false; // initial value for red test
    }

    public boolean sameDomain(String page, String domain) {
        if (page.contains("http") || page.contains("https")) {
            if (page.contains(domain)) return true;
            return false;
        }
        return true;
    }

    // This method to get the page content might be recursive
    public Set<String> getPagesFromUrl(String url) {
//        System.out.println("url = "+url);
        int start = url.indexOf("://");
        String protocol = url.substring(0,start);
        String domain = url.substring(start+3);
//        System.out.println("in getPageFromUrl with : "+domain);
        String html = getPageContent(url);
        Set<String> pages = new HashSet<>();
        pages.add(url);
        HtmlPageExtractor htmlPageExtractor = new HtmlPageExtractor();
        Vector<HtmlPage> extractedPages = htmlPageExtractor.grabHtmlPages(html);
        for (HtmlPage htmlPage : extractedPages) {
            String page = htmlPage.page;
            if (!pages.contains(page) && sameDomain(page,domain)) {
                pages.add(page);
                // take care of subPages - need test
                if (!page.contains(domain)) {
                    String fullPage = protocol+"://"+domain;
                    fullPage += !page.startsWith("/") ? "/" : "";
                    fullPage += page;
                    String pageHtml = getPageContent(url);
                    Vector<HtmlPage> morePages = htmlPageExtractor.grabHtmlPages(pageHtml);
                    for (HtmlPage morePage : morePages) {
                        String subPage = htmlPage.page;
                        if (!pages.contains(subPage)) {
                            pages.add(subPage);
                        }
                    }
                }
            }
        }
        return pages;
//        return null; // initial value for red test
    }
}

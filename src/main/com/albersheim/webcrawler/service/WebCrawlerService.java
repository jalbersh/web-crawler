package com.albersheim.webcrawler.service;

import com.albersheim.webcrawler.utils.StringBufferOutputStream;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebCrawlerService {

    // This method to get the page content might be recursive
    public String getPageContent(String url) {
        if (!checkUrlIsValidFormat(url)) return null;
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

    public List<String> getPagesInPage(String html, String url) {
        List<String> pages = new ArrayList<>();
        if (html != null && html.length()>0) {
            // assume pages are all hrefs
            String[] parts = html.split("href");
            for (String part : parts) {
                if (part.contains(url)) { // assume only want pages under original url base
                    System.out.println("part=" + part);
                    // strip off all urls between quotes
                    int start = part.indexOf("\"");
                    int end = part.indexOf("\"", start + 1);
                    String page = part.substring(start, start + end);
                    System.out.println("part=" + part);
                    pages.add(page);
                }
            }
        }
        return pages;
//        return null; // initial value for red test
    }
}

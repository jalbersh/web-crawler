package com.albersheim.webcrawler.service;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WebCrawlerService {

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

    public String getPageFromHrefContent(String hrefContent) {
        String page=null;
        int start = hrefContent.indexOf("href=\"");
        int end = hrefContent.indexOf("\">");
        page = hrefContent.substring(start+"href=\"".length(),end);
        return page;
//        return null; // initial for red test
    }

    // This method to get the page content might be recursive
    public List<String> getPagesFromUrl(String url) {
        String html = getPageContent(url);
        List<String> pages = new ArrayList<>();
        if (html != null && html.length()>0) {
            // assume pages are all hrefs
            Pattern linkPattern = Pattern.compile("(<a[^>]+>.+?<\\/a>)");//,  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
            Matcher pageMatcher = linkPattern.matcher(html);
            while(pageMatcher.find()){
                String page = getPageFromHrefContent(pageMatcher.group());
                System.out.println("adding page: "+page);
                pages.add(page);
            }
        }
        return pages;
//        return null; // initial value for red test
    }
}

package com.albersheim.webcrawler.service;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String stripOffExtraneousCharacters(String hrefContent) {
        String page=null;
        int start = hrefContent.indexOf("href=\"");
        int end = hrefContent.indexOf(">");
        if (start == -1) start = 0;
        if (end == -1) end = hrefContent.length();
        page = hrefContent.substring(start+"href=\"".length(),end);
        // strip off \'s
        end = page.indexOf("\\");
        if (end > 0) page = page.substring(0,end);
        // strip off #'s
        end = page.indexOf("#");
        if (end > 0) page = page.substring(0,end);
        // strip off last /?s
        end = page.indexOf("/?s");
        if (end > 0) page = page.substring(0,end);
        // strip off last part after target
        end = page.indexOf(" target");
        if (end > 0) page = page.substring(0,end);
        // strip off last part after title
        end = page.indexOf(" title");
        if (end > 0) page = page.substring(0,end);
        // strip off /\""
        end = page.indexOf("/\\\"");
        if (end > 0) page = page.substring(0,end);
         // strip off last characters
        if (page.endsWith("\"")) page = page.substring(0,page.length()-1);
        if (page.endsWith("\\\"")) page = page.substring(0,page.length()-2);
        // strip off last /
        if (page.endsWith("/")) page = page.substring(0,page.length()-1);
        return page;
//        return null; // initial for red test
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
        String domain = url.substring(start+3);
//        System.out.println("in getPageFromUrl with : "+domain);
        String html = getPageContent(url);
        Set<String> pages = new HashSet<>();
        pages.add(url); // add initial url
        if (html != null && html.length()>0) {
            // assume pages are all hrefs
            Pattern linkPattern = Pattern.compile("(<a[^>]+>.+?<\\/a>)",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
            Matcher pageMatcher = linkPattern.matcher(html);
            while(pageMatcher.find()){
                String page = stripOffExtraneousCharacters(pageMatcher.group());
                if (!pages.contains(page) && sameDomain(page,domain)) {
                    pages.add(page);
                }
            }
        }
        return pages;
//        return null; // initial value for red test
    }
}

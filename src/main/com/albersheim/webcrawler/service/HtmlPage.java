package com.albersheim.webcrawler.service;

class HtmlPage {

    String page;
    String pageText;

    HtmlPage(){};

    @Override
    public String toString() {
        return new StringBuffer("Link : ").append(this.page)
                .append(" Link Text : ").append(this.pageText).toString();
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = replaceInvalidChar(page);
    }

    public String getPageText() {
        return pageText;
    }

    public void setPageText(String pageText) {
        this.pageText = pageText;
    }

    private String replaceInvalidChar(String page){
        page = page.replaceAll("'", "");
        page = page.replaceAll("\"", "");
        return page;
    }

}

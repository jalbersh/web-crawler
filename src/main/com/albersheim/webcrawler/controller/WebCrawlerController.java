package com.albersheim.webcrawler.controller;

import com.albersheim.webcrawler.service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin()
public class WebCrawlerController {
	@Autowired
	private WebCrawlerService webCrawlerService;

	public WebCrawlerController(WebCrawlerService webCrawlerService) {
		this.webCrawlerService = webCrawlerService;
	}

	@GetMapping({"/hello"})
	public String hello() {
		return "Hello World";
	}

	@RequestMapping(value = "/getSiteMap", method = RequestMethod.GET)
	public Set<String> getSiteMap(@RequestParam String url) {
		System.out.println("in getSiteMap with url="+url);
		String html = webCrawlerService.getPageContent(url);
		Set<String> pages = webCrawlerService.getPagesFromUrl(url);
		return pages;
		//		return null; // initial red test
	}
}
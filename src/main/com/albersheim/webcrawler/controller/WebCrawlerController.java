package com.albersheim.webcrawler.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
public class WebCrawlerController {

	@GetMapping({ "/hello" })
	public String hello() {
		return "Hello World";
	}

}

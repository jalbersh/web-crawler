package com.albersheim.webcrawler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(locations={"classpath:app-context.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class WebCrawlerControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private WebCrawlerController con;

    private MockMvc mvc;

    @Before
    public void setup() {
        System.out.println("in setup");
        con = new WebCrawlerController();
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        System.out.println("in tearDown");
        con = null;
        mvc = null;
    }

    @Test
    public void testHelloSucceeds() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MockHttpServletRequestBuilder request = get("/hello")
                .contentType(MediaType.TEXT_PLAIN);
        this.mvc.perform(request)
                .andExpect(status().isOk());
        String formatted = con.hello();
        assertThat(formatted.equals("Hello World"));
    }

}
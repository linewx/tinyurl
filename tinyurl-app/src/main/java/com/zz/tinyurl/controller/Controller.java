package com.zz.tinyurl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RestController
public class Controller {
    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Value("${kgsServer}")
    String kgsServer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/urls")
    public String add(@RequestBody String url) {

        String shortUrl = null;
        try {
            shortUrl = restTemplate.getForObject(kgsServer, String.class);
        } catch (RestClientException e) {
            logger.error("kgs server is wrong", e);
        }
        try {
            jdbcTemplate.execute(String.format("insert into url (origurl, shorturl) values('%s', '%s')", url, shortUrl));
        } catch (DataAccessException e) {
            logger.error("failed to insert url to database", e);
        }
        return shortUrl;
    }

    @GetMapping("/{shorturl}")
    public void queryUrl(@PathVariable("shorturl") String shorturl, HttpServletResponse httpServletResponse) {
        try {
            String originUrl = jdbcTemplate.queryForObject(String.format("select origurl from url where shorturl='%s'", shorturl), String.class);
            httpServletResponse.setHeader("Location", originUrl);
            httpServletResponse.setStatus(302);
        }catch (Exception e) {
            httpServletResponse.setStatus(404);
        }

    }
}

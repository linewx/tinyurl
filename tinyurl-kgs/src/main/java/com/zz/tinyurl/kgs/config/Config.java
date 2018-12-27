package com.zz.tinyurl.kgs.config;

import com.zz.tinyurl.kgs.RandomString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
public class Config {
    @Bean
    public RandomString randomString(){
        return new RandomString(6, new Random());
    }

    @Bean
    public ConcurrentLinkedQueue<String> keyQueue() {
        return new ConcurrentLinkedQueue<>();
    }

}

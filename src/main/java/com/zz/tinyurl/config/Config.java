package com.zz.tinyurl.config;

import com.zz.tinyurl.RandomString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class Config {
    @Bean
    public RandomString randomString(){
        return new RandomString(6, new Random());
    }
}

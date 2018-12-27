package com.zz.tinyurl.kgs.controller;


import com.zz.tinyurl.kgs.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
public class Controller {
    @Autowired
    ConcurrentLinkedQueue<String> keyQueue;

    @Value("${bulkSize}")
    Integer bulkSize;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String getUrl() {
        String key = keyQueue.poll();
        if (key == null) {
            // feed the queue
            synchronized (Controller.class) {
                key = keyQueue.poll();
                if (key == null) {
                    //enqueue keys
                    List<String> keys = jdbcTemplate.query("select url from urlkey limit " + bulkSize.toString(),
                            (resultSet, i) -> resultSet.getString("url"));
                    jdbcTemplate.execute("update urlkey set used=1 where url in " + "('" + String.join("','", keys) + "')" );
                    keyQueue.addAll(keys);
                    return keyQueue.poll();
                }else {
                    return key;
                }
            }

        }else {
            return key;
        }
    }
}

package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Service {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("secondJdbcTemplate")
    JdbcTemplate secondJdbcTemplate;

    public void insertData() {
        System.out.println("=============From data 1=================");

        List<Map<String, Object>> dataList = jdbcTemplate.queryForList("select count(*) from Goods;");
        for (Map<String, Object> data: dataList) {
            for (String key : data.keySet()) {
                System.out.println("key: " + key + " value: " + data);
            }
        }
    }


    public void readData() {
        System.out.println("=================From data 2============");
        List<Map<String, Object>> dataList = secondJdbcTemplate.queryForList("select  count(*) from Goods;");
        for (Map<String, Object> data: dataList) {
            for (String key : data.keySet()) {
                System.out.println("key: " + key + " value: " + data);
            }
        }
    }
}

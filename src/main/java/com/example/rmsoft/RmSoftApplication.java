package com.example.rmsoft;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.rmsoft.mapper")
public class RmSoftApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmSoftApplication.class, args);
    }

}

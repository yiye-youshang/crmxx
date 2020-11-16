package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.repository")
@SpringBootApplication
public class KissproductApplication {

    public static void main(String[] args) {
        SpringApplication.run(KissproductApplication.class, args);
    }

}

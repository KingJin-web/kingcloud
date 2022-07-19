package com.king.kingcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


//@SpringBootApplication
@SpringBootApplication()

public class KingCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(KingCloudApplication.class, args);
    }

}

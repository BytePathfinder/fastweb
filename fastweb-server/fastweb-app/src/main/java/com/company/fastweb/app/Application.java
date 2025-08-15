package com.company.fastweb.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FastWeb应用启动类
 */
@SpringBootApplication(scanBasePackages = "com.company.fastweb")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
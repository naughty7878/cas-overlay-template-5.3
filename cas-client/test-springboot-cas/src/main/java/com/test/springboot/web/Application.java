package com.test.springboot.web;

import org.jasig.cas.client.boot.configuration.EnableCasClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

@EnableCasClient
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // Spring应用启动
        SpringApplication.run(Application.class, args);
    }

}

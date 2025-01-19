package com.test.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @SpringBootApplication 用来标注一个主程序，说明这是一个Spring Boot应用
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        // Spring应用启动
        SpringApplication.run(Application.class, args);
    }
}

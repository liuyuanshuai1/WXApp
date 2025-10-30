package com.familypoints.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 家庭积分小程序后端应用主类
 * 这是Spring Boot应用的入口点
 */
@SpringBootApplication
@ComponentScan("com.familypoints.backend")
public class FamilyPointsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyPointsApplication.class, args);
    }

}
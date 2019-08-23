package com.example.elasticsearch;

import com.example.elasticsearch.controller.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {UserController.class}, scanBasePackages = "com.example.elasticsearch")
public class ElasticSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }

}

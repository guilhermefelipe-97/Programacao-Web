package com.meuecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.meuecommerce"})
public class MeuecommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeuecommerceApplication.class, args);
    }
}
package com.example.untoldpsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication

public class UntoldPsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(UntoldPsProjectApplication.class, args);
    }
}

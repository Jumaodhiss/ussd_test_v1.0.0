package com.pocco.pl.ussdtest;

import com.pocco.pl.ussdtest.params.LoanParams;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UssdtestApplication {

    public static void main(String[] args) {
        new LoanParams().setLoanTypes();
        SpringApplication.run(UssdtestApplication.class, args);
    }
}

package com.karmazyn.logisticsdispatchsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogisticsDispatchSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsDispatchSystemApplication.class, args);
    }

}

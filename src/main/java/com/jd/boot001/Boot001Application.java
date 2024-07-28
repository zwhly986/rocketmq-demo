package com.jd.boot001;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = "com.jd.boot001.*")
@MapperScan(value = {"com.jd.boot001.**.mapper"})
public class Boot001Application {

    public static void main(String[] args) {
        SpringApplication.run(Boot001Application.class, args);
    }

}

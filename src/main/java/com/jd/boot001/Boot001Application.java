package com.jd.boot001;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = "com.jd.boot001.*")
//@MapperScan(value = {"com.jd.boot001.**.mapper"})
@MapperScan(value = {"com.jd.boot001.mapper"}) // 有此注解，则不需要在保包中mapper接口上添加@Mapper或@Repository
@EnableScheduling
public class Boot001Application implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Boot001Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Boot001Application.class, args);
    }

    /**
     * 在该方法中添加Springboot成功后要执行的操作，也可实现接口 ApplicationRunner
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("SpringBoot启动成功");
        // 在此添加Springboot启动后要执行的操作 // TODO: 2024/8/1
    }

}

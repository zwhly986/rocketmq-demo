package com.jd.boot001.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Druid数据库连接池配置
 * 参考：https://blog.csdn.net/weixin_37799575/article/details/125102566
 */
@Slf4j
@Configuration
public class DruidDatasrouceConfig {
    private static final Logger log = LoggerFactory.getLogger(DruidDatasrouceConfig.class);

    /**
     * DruidDatasrouceConfig
     * @return DataSource
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();

        log.info("Datasource创建完成 ...");
        log.info(druidDataSource.toString());

        return druidDataSource;
    }
}

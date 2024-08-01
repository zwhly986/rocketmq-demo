package com.jd.boot001.config;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义RocketMQ
 */
/*
@Configuration
public class RocketMQConfig {

    @Bean
    public RocketMQTemplate rocketMQTemplate(Producer producer) {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        rocketMQTemplate.setProducer(producer);
        // 可以在这里添加自定义的MessageConverter
        // rocketMQTemplate.setMessageConverter(new MyCustomMessageConverter());
        return rocketMQTemplate;
    }

    @Bean
    public Producer producer() {
        Producer producer = new Producer();
        // 设置生产者的属性
        producer.setSendMessageTimeout(3000);
        producer.setCompressMessageBodyThreshold(1024);
        producer.setRetryAnotherBrokerWhenSendFailed(true);
        return producer;
    }
}*/

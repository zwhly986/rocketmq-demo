package com.jd.boot001.listener;
/*

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.*;
import org.apache.rocketmq.client.apis.message.MessageView;
*/

import apache.rocketmq.v2.FilterExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;


/**
 * RocketMQ消费者测试
 */
@Component
public class ConsumerTest {
    private static final Logger log = LoggerFactory.getLogger(ConsumerTest.class);


    /*@PostConstruct
    public void doPostConstruct() throws ClientException {
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        String topic = "delayTopic";
        FilterExpression filterExpression = new FilterExpression();//"YourFilterTag", FilterExpressionType.TAG);
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                // 设置消费者分组。
                .setConsumerGroup("YourConsumerGroup")
                // 设置接入点。
                //.setClientConfiguration(ClientConfiguration.newBuilder().setEndpoints("YourEndpoint").build())
                .setClientConfiguration(ClientConfiguration.newBuilder().setEndpoints("192.168.150.72:9876").build())
                // 设置预绑定的订阅关系。
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                // 设置消费监听器。
                .setMessageListener(new MessageListener() {
                    @Override
                    public ConsumeResult consume(MessageView messageView) {

                        String str = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();

                        log.info("消费MQ消息：", messageView.getMessageId(), messageView.getTopic(), str);


                        // 消费消息并返回处理结果。
                        return ConsumeResult.SUCCESS;
                    }
                })
                .build();

    }*/



}

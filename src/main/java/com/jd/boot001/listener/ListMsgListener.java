package com.jd.boot001.listener;

import com.jd.boot001.entity.Order;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RocketMQ集合消息监听器
 */
@Component
@RocketMQMessageListener(topic = "listTopic",
        consumerGroup = "list-topic-consumer")
public class ListMsgListener implements RocketMQListener<List<Order>> {
    private static final Logger log = LoggerFactory.getLogger(ListMsgListener.class);

    @Override
    public void onMessage(List<Order> orders) {
        log.info("接收到集合消息：");

        orders.forEach(o -> {
            log.info("接收到集合消息[{}]：{}", Thread.currentThread().getName(), o);
        });
    }

}
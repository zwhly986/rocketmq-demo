package com.jd.boot001.listener;

import com.jd.boot001.entity.Order;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * RocketMQ顺序消息监听器
 */
@Component
/*@RocketMQMessageListener(
        topic = "orderlyMsgTopic",
        consumerGroup = "boot-mq-group-consumer",
        consumeMode = ConsumeMode.ORDERLY
)*/
public class OrderMsgListener implements RocketMQListener<Order> {
    private static final Logger log = LoggerFactory.getLogger(OrderMsgListener.class);

    @Override
    public void onMessage(Order message) {
        log.info("收到顺序消息：{}", message);
    }

}
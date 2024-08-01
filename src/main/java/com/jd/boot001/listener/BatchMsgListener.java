package com.jd.boot001.listener;

import com.jd.boot001.entity.Order;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 发送批量消息--监听器
 */
@Component
@RocketMQMessageListener(topic = "batchOrderly",
        consumerGroup = "batch-orderly-consumer")
public class BatchMsgListener implements RocketMQListener<Order> {
    private static final Logger log = LoggerFactory.getLogger(BatchMsgListener.class);

    @Override
    public void onMessage(Order message) {
        log.info(Thread.currentThread().getName() + " 批量消息监听器收到消息：" + message);
    }

}
package com.jd.boot001.listener;

import com.jd.boot001.common.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * RocketMQ消费者
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = Const.ORDERLY_TOPIC, consumerGroup = Const.ORDERLY_CONSUMER_GROUP,
        consumeMode = ConsumeMode.ORDERLY)
public class OrderMessageConsumer implements RocketMQListener<String> {
    private static final Logger log = LoggerFactory.getLogger(OrderMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        log.info("订单消费者接收到的消息: {}", message);

        try {
            //延时5秒，看是否消费完成才继续消费下一个
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error("RocketMQ消息消费异常", e);
        }
    }

}

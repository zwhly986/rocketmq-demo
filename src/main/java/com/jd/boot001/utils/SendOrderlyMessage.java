package com.jd.boot001.utils;

import com.jd.boot001.common.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 顺序消息发送
 *  参考：https://blog.csdn.net/Fire_Sky_Ho/article/details/136291027
 */
@Slf4j
@Component
public class SendOrderlyMessage {
    private static final Logger log = LoggerFactory.getLogger(SendOrderlyMessage.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 同步顺序消息
     */
    public void sync() {
        String message = "我是一条同步顺序消息:";
        for (int i = 1; i <= 5; i++) {
            // hashkey是为了确保这些消息被路由到同一个消息队列，这样消费者就能够按照顺序处理它们
            rocketMQTemplate.syncSendOrderly(Const.ORDERLY_TOPIC, message + i, Const.SYNC_ORDERLY_HASH_KEY);
        }
    }

    /**
     * 异步顺序消息
     */
    public void async() {
        String message = "我是一条异步顺序消息:";
        for (int i = 0; i < 5; i++) {
            // hashkey是为了确保这些消息被路由到同一个消息队列，这样消费者就能够按照顺序处理它们
            int finalI = i;
            rocketMQTemplate.asyncSendOrderly(Const.ORDERLY_TOPIC, message + i, Const.ASYNC_ORDERLY_HASH_KEY, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    // 异步发送成功的回调逻辑
                    log.info("async|异步顺序消息【{}】发送成功: {}", finalI, sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    // 异步发送失败的回调逻辑
                    log.info("async|异步顺序消息【{}】发送失败: {}", finalI, e.getMessage());
                }
            });
//            try {
//                Thread.sleep(200L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}

package com.jd.boot001.listener;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 同步消息--监听器
 */
@Component
//@RocketMQMessageListener(topic = "syncTopic", consumerGroup = "sync-topic-group-consumer")
public class SyncMsgListener implements RocketMQListener<MessageExt> {
    private static final Logger log = LoggerFactory.getLogger(SyncMsgListener.class);

    @Override
    public void onMessage(MessageExt message) {
        log.info("同步消息接收：{}", JSON.toJSONString(message));

        String msgId = message.getMsgId();
        String msg = new String(message.getBody());
        log.info("同步消息id：{}，消息内容：{}", msgId, msg);
    }

}


package com.jd.boot001.listener;

import com.alibaba.fastjson.JSON;
import com.jd.boot001.utils.DateUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 延迟消息--监听器
 * // TODO: 2024/8/3 没有手动提交消费结果 
 */
@Component
//@RocketMQMessageListener(topic = "delayTopic", consumerGroup = "delay-topic-group-consumer")
public class DelayMsgListener implements RocketMQListener<MessageExt> {
    private static final Logger log = LoggerFactory.getLogger(DelayMsgListener.class);

    @Override
    public void onMessage(MessageExt message) {
        //log.info("延迟消息接收[{}]：{}", DateUtils.date(), JSON.toJSONString(message));

        String msgId = message.getMsgId();
        String msg = new String(message.getBody());
        log.info("消息id：{}，消息内容：{}，消息收到时间：{}", msgId, msg, DateUtils.date());
    }

}


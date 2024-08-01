package com.jd.boot001.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * RocketMQ监听器
 */
@Slf4j
@Component
//@RocketMQMessageListener(topic = "delayTopic", consumerGroup = "boot-mq-group-consumer")
// topic:要订阅的RocketMQ主题
// selectorExpression：指定标签（Tag），selectorExpression属性设置的标签与消息的标签相匹配时，onMessage方法才会被调用
// consumerGroup：费者组的名称
@RocketMQMessageListener(
        topic = "tagTopic", // 消息主题
        selectorType = SelectorType.TAG,
        selectorExpression = "java", // 消息标签，主题相同，且消费者订阅的Tag和发送者设置的消息Tag相互匹配，则消息被投递给消费端进行消费
        consumerGroup = "boot-mq-group-consumer"  // 消费者组
)
public class MQMsgListener implements RocketMQListener<MessageExt> {
    private static final Logger log = LoggerFactory.getLogger(MQMsgListener.class);

    @Override
    public void onMessage(MessageExt message) {
        log.info("接收到的RocketMQ消息：{}", JSON.toJSONString(message));

        String msgId = message.getMsgId();
        String msg = new String(message.getBody());
        log.info("消息id：{}，消息内容：{}", msgId, msg);
    }

}

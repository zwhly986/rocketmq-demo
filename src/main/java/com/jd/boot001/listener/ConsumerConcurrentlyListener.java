package com.jd.boot001.listener;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 并发接收消息，手动确认
 * 顺序接收消息参考ConsumerOrderlyListener
 */
@Component
public class ConsumerConcurrentlyListener {
    private static final Logger log = LoggerFactory.getLogger(ConsumerConcurrentlyListener.class);

    @Value("${rocketmq.name-server}")
    private String nameServer;

    /**
     * 消费者组
     */
    @Value("${rocketmqConsumerGroup:test_consumer_group}")
    private String consumerGroup;

    @PostConstruct  // 依赖注入完成后执行该方法
    // 或者用注解 @EventListener(ApplicationReadyEvent.class) // 应用程序启动后运行该方法
    public void onPostConstruct() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameServer);
        consumer.setInstanceName("rmq-instance");
        // 参数一：topic，参数二：标签
        consumer.subscribe("tagTopic", "java");

        // 方法一：并发消费
        /*consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    log.info("并发消费接收到数据：" + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                //return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                //return null; // 不断重复消费，不要用
            }
        });*/

        // 方法二：顺序消费
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgList, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgList) {
                    log.info("顺序消费接收到数据：" + new String(msg.getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
                //return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                //return null;
            }
        });

        consumer.start();
        log.info("RocketMQ消费者启动成功");
    }

}
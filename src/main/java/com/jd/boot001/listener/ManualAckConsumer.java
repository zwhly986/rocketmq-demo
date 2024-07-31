package com.jd.boot001.listener;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * RocketMQ消费手动确认，可行
 * Demo测试用，逻辑迁移至ManualAckConsumerListener
 */
@Deprecated
public class ManualAckConsumer {

    public static void main(String[] args) throws MQClientException {
        // 创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_group_txmsg_bank2");
        // 指定Namesrv地址
        consumer.setNamesrvAddr("192.168.199.151:9876");
        // 设置Consumer第一次启动是从队列头部开始消费还是尾部开始
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅主题和标签
        consumer.subscribe("topic_txmsg", "*");

        // 注册消息监听器
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                // 处理消息
                for (MessageExt msg : msgs) {
                    System.out.println("收到RocketMQ消息：" + new String(msg.getBody()));
                }

                // 手动确认消息
                // 根据业务逻辑决定是否成功消费消息
//                return ConsumeOrderlyStatus.SUCCESS; // 成功消费

                // 将不断重复消费未确认的消息
                // return null;

                // 可选的返回值有：
                // return ConsumeOrderlyStatus.SUCCESS; // 成功消费
                // return ConsumeOrderlyStatus.FAILED; // 消费失败，将重新消费
                return ConsumeOrderlyStatus.COMMIT; // 提交消费位置，不重新消费
                //return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT; // 暂停当前队列的消费
            }
        });

        // 启动消费者
        consumer.start();
        System.out.println("RocketMQ消费者启动成功");
    }
}
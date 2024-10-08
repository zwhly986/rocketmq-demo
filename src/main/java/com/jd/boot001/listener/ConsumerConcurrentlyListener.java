package com.jd.boot001.listener;


import com.jd.boot001.utils.DateUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 在消费者初始化时注册一个消费监听器，在消费监听器内部实现消息处理逻辑
 * 并发接收消息，手动确认
 * 顺序接收消息参考ConsumerOrderlyListener
 *
 * broker:中介性代理，是一种消息中间件软件,它负责接收、处理、分发应用程序之间的消息
 * 消费者类型：
 * PullConsumer：在流处理框架中集成使用
 * PushConsumer
 * SimpleConsumer
 * 生产环境中相同的 ConsumerGroup 下严禁混用 PullConsumer 和其他两种消费者，否则会导致消息消费异常
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
        // 推消息消费
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        // 指定nameserver的地址
        consumer.setNamesrvAddr(nameServer);
        //设置实例名称，一个jvm中有多个消费者可以根据实例名区分，默认DEFAULT
        consumer.setInstanceName("rmq-instance");
        // 参数一：topic，参数二：标签
        //consumer.subscribe("tagTopic", "java");
        consumer.subscribe("delayTopic", "*"); // 所有标签

        // 方法一：并发消费
        /*
        // 1 提高消费并行度
        consumer.setConsumeThreadMax(10);
        consumer.setConsumeThreadMin(1);

        // 2 以批量方式进行消费 //设置消息批处理的一个批次中消息的最大个数
        consumer.setConsumeMessageBatchMaxSize(10);
        // 设置重试次数 默认16次
        consumer.setMaxReconsumeTimes(1);

        // 并发消费
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    log.info("并发消费接收到数据：" + new String(msg.getBody()));
                    try {
						// 业务处理
					} catch (Exception e) {
                        log.error("业务处理异常", e);
					}
                }
                // 消费状态提交：消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                // 消费状态提交：消费失败
                //return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                //return null; // 不断重复消费，不要用
            }
        });*/

        // 方法二：顺序消费
        // 1 提高消费并行度
        consumer.setConsumeThreadMax(1); // 10
        consumer.setConsumeThreadMin(1);

        // 2 以批量方式进行消费 //设置消息批处理的一个批次中消息的最大个数
        consumer.setConsumeMessageBatchMaxSize(20); // 10
        // 设置重试次数 默认16次
        consumer.setMaxReconsumeTimes(1);

        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgList, ConsumeOrderlyContext context) {
                MessageQueue messageQueue = context.getMessageQueue();

                // 读到的数据是乱序的
                for (MessageExt msg : msgList) {
//                    log.info("顺序消费数据：" + new String(msg.getBody()));
                    log.info("消费delayTopic消息：{}---[消费时间：{}]", new String(msg.getBody()), DateUtils.date());
                    // TODO: 2024/8/4 在消费监听器内部实现消息处理逻辑
                    
                }
                // 提交消费结果：消费成功
                return ConsumeOrderlyStatus.SUCCESS;
                // 提交消费结果：消费失败
                //return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        });

        consumer.start();
        log.info("RocketMQ消费者启动成功");
    }  // 方法end



}



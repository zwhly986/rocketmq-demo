package com.jd.boot001.service.impl;

import com.jd.boot001.common.Const;
import com.jd.boot001.common.R;
import com.jd.boot001.service.SendMQService;
import com.jd.boot001.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.apache.rocketmq.common.message.Message;

import java.io.UnsupportedEncodingException;
import java.util.Random;


/**
 * RocketMQ消息发送
 * <p>
 * 顺序消息发送 参考：https://blog.csdn.net/Fire_Sky_Ho/article/details/136291027
 */
@Slf4j
@Service
public class SendMQServiceImpl implements SendMQService {
    private static final Logger log = LoggerFactory.getLogger(SendMQServiceImpl.class);

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Value("${rocketmq.name-server}")
    private String nameServer;

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


    /**
     * 延迟消息发送
     *
     * @param message
     */
    public R sendDelayMsg(String message) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.start();

        try {
            // 延迟时间随机生成
            Random random = new Random();
            // 生成一个随机整数，范围在0（包含）和10（不包含）之间
            int dalayTime = random.nextInt(100);

            // 消息内容
            StringBuilder sb = new StringBuilder(message);
            sb.append("[").append(DateUtils.date()).append("]")
                    .append("延迟 ").append(dalayTime).append(" 秒");


            // 参数一：主题，参数二：标签，参数三：消息内容
            Message msg = new Message(
                    "delayTopic", // 主题
                    "checkPayment", // 标签
                    (sb.toString()).getBytes(RemotingHelper.DEFAULT_CHARSET) // 消息体
            );

            // 设置延迟时间，单位秒
            msg.setDelayTimeSec(dalayTime); // 单位：秒
            // msg.setDelayTimeMs(90); // 单位：毫秒

            // Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            log.info("延迟消息发送结果：{}", sendResult);

            if (SendStatus.SEND_OK == sendResult.getSendStatus()) {
                return R.ok("延迟消息发送成功");
            }
        } finally {
            // Shut down once the producer instance is not longer in use.
            producer.shutdown();
        }

        return R.error("延迟消息发送失败");
    }

/*
    public R sendDelayMsg2(String message0) throws Exception {
        //定时/延时消息发送
        MessageBuilder messageBuilder = new MessageBuilderImpl();;
        //以下示例表示：延迟时间为10分钟之后的Unix时间戳。
        Long deliverTimeStamp = System.currentTimeMillis() + 10L * 60 * 1000;
        Message message = messageBuilder.setTopic("topic")
                //设置消息索引键，可根据关键字精确查找某条消息。
                .setKeys("messageKey")
                //设置消息Tag，用于消费端根据指定Tag过滤消息。
                .setTag("messageTag")
                .setDeliveryTimestamp(deliverTimeStamp)
                //消息体
                .setBody("messageBody".getBytes())
                .build();
        try {
            //发送消息，需要关注发送结果，并捕获失败等异常。
            SendReceipt sendReceipt = producer.send(message);
            System.out.println(sendReceipt.getMessageId());
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return R.ok();
    }*/




}

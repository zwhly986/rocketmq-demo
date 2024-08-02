package com.jd.boot001.listener;

import com.alibaba.fastjson.JSONObject;
import com.jd.boot001.entity.AccountChangeEvent;
import com.jd.boot001.service.Bank2AccountInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消费手动确认，可行 // TODO: 2024/8/2 并发接收消息参考MQConsumer
 */
@Component
public class ConsumerOrderlyListener {
    private static final Logger log = LoggerFactory.getLogger(ConsumerOrderlyListener.class);

    @Value("${rocketmq.name-server}")
    private String rocketmqNameServer;

    @Value("${rocketmqConsumerGroup:consumer_group_txmsg_bank2}")
    private String rocketmqConsumerGroup;

    @Value("${bankTransferTopic:topic_txmsg}")
    private String bankTransferTopic;

    @Autowired
    private Bank2AccountInfoService bank2AccountInfoService;


    /**
     * @throws MQClientException
     * @EventListener注解：指定Springboot启动后要执行的方法，实现同样的功能可以实现CommandLineRunner或ApplicationRunner接口
     */
    @EventListener(ApplicationReadyEvent.class)  // 或者@PostConstruct //依赖注入完成后被自动调用，方法不能有参数，必须是非静态
    public void onApplicationReady() throws MQClientException {
        // SpringBoot启动完成后要执行的操作
        log.info("springboot启动完成，注册RocketMQ消费者");

        // 创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketmqConsumerGroup);
        // 指定Namesrv地址
        consumer.setNamesrvAddr(rocketmqNameServer);
        // 设置Consumer第一次启动是从队列头部开始消费还是尾部开始
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅主题和标签
        consumer.subscribe(bankTransferTopic, "*");

        // 注册消息监听器  并发接收消息使用 MessageListenerConcurrently
        consumer.registerMessageListener(new MessageListenerOrderly() {

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

                int successNum = 0;

                // 处理消息
                for (MessageExt msg : msgs) {
                    String message = new String(msg.getBody());
                    log.info("银行转账事务消息消费开始，收到RocketMQ消息：{}", message);

                    //解析消息
                    JSONObject jsonObject = JSONObject.parseObject(message);
                    String accountChangeString = jsonObject.getString("accountChange");
                    //转成AccountChangeEvent
                    AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);

                    // 收款账号
                    String toAccountNo = accountChangeEvent.getToAccountNo();
                    // 转账金额
                    Double amount = accountChangeEvent.getAmount();

                    if (StringUtils.isBlank(toAccountNo)) {
                        log.info("转入账户不能为空");
                        return null;
                    }
                    if (amount == null) {
                        log.info("转账金额不能为空");
                        return null;
                    }

                    try {
                        //更新本地账户，增加金额
                        boolean isSuccess = bank2AccountInfoService.addAccountInfoBalance(accountChangeEvent);
                        log.info("银行转账事务消息消费，本地账户更新结果：{}", isSuccess);

                        if (isSuccess) {
                            successNum++;
                        }

                    } catch (Exception e) {
                        log.error("银行转账事务消息消费，本地账户更新失败，{}", e.getMessage(), e);
                    }

                    log.info("银行转账事务消息消费结束");
                }  // for end

                // 手动确认消息

                if (successNum == msgs.size()) {
                    // 根据业务逻辑决定是否成功消费消息
                    return ConsumeOrderlyStatus.SUCCESS; // 该批次消息全部消费成功，则手动提交消费成功，否则，需要该批次消息需要再次重新消费，所以消费方法要幂等
                }

                // 将不断重复消费未确认的消息
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                //return null;

                // 可选的返回值有：
                // return ConsumeOrderlyStatus.SUCCESS; // 成功消费
                // return ConsumeOrderlyStatus.FAILED; // 消费失败，将重新消费
                // return ConsumeOrderlyStatus.COMMIT; // 提交消费位置，不重新消费
                //return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT; // 暂停当前队列的消费
            }
        });

        // 启动消费者
        consumer.start();
        log.info("RocketMQ消费者启动成功");
    }

}
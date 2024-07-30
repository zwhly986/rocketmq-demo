package com.jd.boot001.listener;


import com.alibaba.fastjson.JSONObject;
import com.jd.boot001.entity.AccountChangeEvent;
import com.jd.boot001.service.Bank2AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 事务消息消费，Consumer端
 * 银行转账
 */
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "consumer_group_txmsg_bank2", topic = "topic_txmsg")
public class TxmsgConsumer implements RocketMQListener<String> {
    private static final Logger log = LoggerFactory.getLogger(TxmsgConsumer.class);

    @Autowired
    private Bank2AccountInfoService bank2AccountInfoService;

    //接收消息
    @Override
    public void onMessage(String message) {
        log.info("银行转账事务消息消费开始，消息：{}", message);

        //解析消息
        JSONObject jsonObject = JSONObject.parseObject(message);
        String accountChangeString = jsonObject.getString("accountChange");
        //转成AccountChangeEvent
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
        //设置账号为李四的
        //accountChangeEvent.setToAccountNo("2"); // TODO: 2024/7/30 不需要在此设置

        try {
            //更新本地账户，增加金额
            boolean isSuccess = bank2AccountInfoService.addAccountInfoBalance(accountChangeEvent);
            log.info("银行转账事务消息消费，本地账户更新结果：{}", isSuccess);
        } catch (Exception e) {
            log.error("银行转账事务消息消费，本地账户更新失败，{}", e.getMessage(), e);
        }

        log.info("银行转账事务消息消费结束");
    }

}

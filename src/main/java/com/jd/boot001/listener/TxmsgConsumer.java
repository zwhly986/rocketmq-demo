package com.jd.boot001.listener;


import com.alibaba.fastjson.JSONObject;
import com.jd.boot001.entity.AccountChangeEvent;
import com.jd.boot001.service.Bank2AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * // TODO: 2024/8/2 该文件不要用，不能手动提交 
 * 事务消息消费，消费者端
 * 银行转账
 */
@Slf4j
@Component
// 可用，不用 @RocketMQMessageListener(consumerGroup = "consumer_group_txmsg_bank2", topic = "topic_txmsg")
// TODO: 2024/7/30 nameServer若读取默认值，可省略该配置项，如下所示
/*@RocketMQMessageListener(
        nameServer="${rocketmq.name-server}",
        consumerGroup = "consumer_group_txmsg_bank2",
        topic = "topic_txmsg"
)*/
public class TxmsgConsumer implements RocketMQListener<String> { // 泛型指定接收到的消息类型为String
    private static final Logger log = LoggerFactory.getLogger(TxmsgConsumer.class);

    @Autowired
    private Bank2AccountInfoService bank2AccountInfoService;

    /**
     * 接收消息，如何手工确认ack？
     * @param message
     */
    @Override
    public void onMessage(String message) {
        log.info("银行转账事务消息消费开始，消息：{}", message);

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
            return;
        }
        if (amount == null) {
            log.info("转账金额不能为空");
            return;
        }

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

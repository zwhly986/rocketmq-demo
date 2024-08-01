package com.jd.boot001.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jd.boot001.entity.AccountChangeEvent;
import com.jd.boot001.mapper.Bank1AccountInfoMapper;
import com.jd.boot001.service.Bank1AccountInfoService;
import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 银行转账
 */
@Slf4j
@Service
public class Bank1AccountInfoServiceImpl implements Bank1AccountInfoService {
    private static final Logger log = LoggerFactory.getLogger(Bank1AccountInfoServiceImpl.class);

    @Autowired
    private Bank1AccountInfoMapper bank1AccountInfoDao;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 向mq发送转账消息（事务消息，半消息）
     */
    @Override
    public TransactionSendResult sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        //将accountChangeEvent转成json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountChange", accountChangeEvent);

        String jsonString = jsonObject.toJSONString();
        //生成message类型
        Message<String> message = MessageBuilder.withPayload(jsonString).build();

        /*
          发送一条事务消息
          String txProducerGroup 生产组
          String destination topic，
          Message<?> message, 消息内容
          Object arg 参数
         */
//        rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_bank1", "topic_txmsg", message, null); // TODO: 2024/7/29 修改前
//        public TransactionSendResult sendMessageInTransaction(String destination, Message<?> message, Object arg)
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction("topic_txmsg", message, null); // TODO: 2024/7/29 修改后
        // ProducerTxmsgListener监听消息发送结果

        log.info("RocketMQ事务消息发送结果：result={}", JSONObject.toJSONString(result));
        return result;
    }


    /**
     * 更新账户，扣减金额
     */
    @Override
    @Transactional
    public boolean doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        log.info("doUpdateAccountBalance,更新账户，扣减金额");

        //幂等判断
        if (bank1AccountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            return false;
        }

        //扣减金额
        int i = bank1AccountInfoDao.updateAccountBalance(accountChangeEvent.getFromAccountNo(), accountChangeEvent.getAmount() * -1);
        //添加事务日志
        int j = bank1AccountInfoDao.addTx(accountChangeEvent.getTxNo());
        log.info("doUpdateAccountBalance,txNo:{}",accountChangeEvent.getTxNo());

        if (accountChangeEvent.getAmount() == 3) {
            throw new RuntimeException("人为制造异常");
        }

        // 成功
        if (i == 1 && j == 1) {
            return true;
        }

        throw new RuntimeException("用户账户金额扣减失败");
    }
}

package com.jd.boot001.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jd.boot001.entity.AccountChangeEvent;
import com.jd.boot001.mapper.Bank1AccountInfoDao;
import com.jd.boot001.service.Bank1AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务消息测试，Producer端添加
 */
@Component
@Slf4j
//@RocketMQTransactionListener(txProducerGroup = "producer_group_txmsg_bank1") // TODO: 2024/7/29 修改前 
@RocketMQTransactionListener//(txProducerGroup = "producer_group_txmsg_bank1")  // TODO: 2024/7/29 修改后
public class ProducerTxmsgListener implements RocketMQLocalTransactionListener {
    private static final Logger log = LoggerFactory.getLogger(ProducerTxmsgListener.class);

    @Autowired
    private Bank1AccountInfoService accountInfoService;

    @Autowired
    private Bank1AccountInfoDao bank1AccountInfoDao;

    //事务消息发送后的回调方法，当消息发送给mq成功，此方法被回调，但是消息还处于未消费状态
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("executeLocalTransaction");
        try {
            //解析message，转成AccountChangeEvent
            String messageString = new String((byte[]) message.getPayload());
            JSONObject jsonObject = JSONObject.parseObject(messageString);
            String accountChangeString = jsonObject.getString("accountChange");
            //将accountChange（json）转成AccountChangeEvent
            AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
            //执行本地事务，扣减金额
            accountInfoService.doUpdateAccountBalance(accountChangeEvent);
            //当返回RocketMQLocalTransactionState.COMMIT，自动向mq发送commit消息，mq将消息的状态改为可消费
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //事务状态回查，查询是否扣减金额
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        //解析message，转成AccountChangeEvent
        String messageString = new String((byte[]) message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(messageString);
        String accountChangeString = jsonObject.getString("accountChange");
        //将accountChange（json）转成AccountChangeEvent
        AccountChangeEvent accountChangeEvent = JSON.parseObject(accountChangeString, AccountChangeEvent.class);
        //事务id
        String txNo = accountChangeEvent.getTxNo();
        log.info("checkLocalTransaction,txNo:{}",txNo);
        int existTx = bank1AccountInfoDao.isExistTx(txNo);
        if (existTx > 0) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}

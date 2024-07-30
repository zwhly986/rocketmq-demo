package com.jd.boot001.service;

import com.jd.boot001.entity.AccountChangeEvent;
import org.apache.rocketmq.client.producer.TransactionSendResult;

/**
 * 事务消息demo
 */
public interface Bank1AccountInfoService {

    /**
     * 向mq发送转账消息
     */
    TransactionSendResult sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);


    /**
     * 更新账户，扣减金额
     */
    boolean doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

}

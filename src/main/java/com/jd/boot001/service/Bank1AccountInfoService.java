package com.jd.boot001.service;

import com.jd.boot001.entity.AccountChangeEvent;

public interface Bank1AccountInfoService {

    /**
     * 向mq发送转账消息
     */
    void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);


    /**
     * 更新账户，扣减金额
     */
    void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

}

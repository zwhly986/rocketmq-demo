package com.jd.boot001.controller;


import com.jd.boot001.common.R;
import com.jd.boot001.entity.AccountChangeEvent;
import com.jd.boot001.service.Bank1AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 用户转账，银行1的账户向银行2的账户转账
 * RocketMQ事务消息测试，Producer端
 */
@RestController
@Slf4j
public class AccountInfoController {
    private static final Logger log = LoggerFactory.getLogger(AccountInfoController.class);

    @Autowired
    private Bank1AccountInfoService accountInfoService;

    // http://localhost:8080/boot001/transfer?accountNo=1&amount=0.11
    @GetMapping(value = "/transfer")
    public R transfer(@RequestParam("accountNo") String accountNo, @RequestParam("amount") Double amount) {
        //创建一个事务id，作为消息内容发到mq
        String tx_no = UUID.randomUUID().toString();
        log.info("transfer,tx_no:{}",tx_no);
        AccountChangeEvent accountChangeEvent = new AccountChangeEvent(accountNo, amount, tx_no);
        //发送消息
        accountInfoService.sendUpdateAccountBalance(accountChangeEvent);
        return R.success().msg("转账成功");
    }
}

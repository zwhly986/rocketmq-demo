package com.jd.boot001.controller;


import com.alibaba.fastjson.JSON;
import com.jd.boot001.common.R;
import com.jd.boot001.entity.AccountChangeEvent;
import com.jd.boot001.service.Bank1AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.TransactionSendResult;
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
 * 2个账户在不同的数据库，这里为了方便放在一个库中，不同的2个表
 */
@RestController
@Slf4j
public class AccountInfoController {
    private static final Logger log = LoggerFactory.getLogger(AccountInfoController.class);

    @Autowired
    private Bank1AccountInfoService bank1AccountInfoService;

    /**
     * http://localhost:8080/boot001/transfer?fromAccountNo=1&toAccountNo=2&amount=0.11
     * accountNo：转出账户
     * amount：转账金额
     */
    @GetMapping(value = "/transfer")
    public R transfer(@RequestParam("fromAccountNo") String fromAccountNo,
                      @RequestParam("toAccountNo") String toAccountNo,
                      @RequestParam("amount") Double amount) {
        // 入参校验
        if (StringUtils.isBlank(fromAccountNo)) {
            return R.error("转出账户不能为空");
        }
        if (StringUtils.isBlank(toAccountNo)) {
            return R.error("转入账户不能为空");
        }
        if (amount == null) {
            return R.error("转账金额不能为空");
        }

        //创建一个事务id，作为消息内容发到mq
        String txNo = UUID.randomUUID().toString();
        log.info("转账，事务ID为txNo:{}",txNo);

        AccountChangeEvent accountChangeEvent = new AccountChangeEvent(fromAccountNo, toAccountNo, amount, txNo);
        // 发送消息（半消息，不可消费，Producer确认提交后Consumer才可消费）
        TransactionSendResult result = bank1AccountInfoService.sendUpdateAccountBalance(accountChangeEvent);
        log.info("转账消息发送结果：" + JSON.toJSONString(result));

        if ("SEND_OK".equals(result.getSendStatus())) {
            return R.success().msg("转账消息发送成功");
        }
        return R.error("转账消息发送失败");
    }

}

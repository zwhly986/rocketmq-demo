package com.jd.boot001.service.impl;

import com.jd.boot001.entity.AccountChangeEvent;
import com.jd.boot001.mapper.Bank2AccountInfoDao;
import com.jd.boot001.service.Bank2AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class Bank2AccountInfoServiceImpl implements Bank2AccountInfoService {
    private static final Logger log = LoggerFactory.getLogger(Bank2AccountInfoServiceImpl.class);

    @Autowired
    private Bank2AccountInfoDao bank2AccountInfoDao;

    //更新账户，增加金额
    @Override
    @Transactional
    public boolean addAccountInfoBalance(AccountChangeEvent accountChangeEvent) {
        log.info("bank2更新本地账号，账号：{},金额：{}", accountChangeEvent.getToAccountNo(), accountChangeEvent.getAmount());

        if (bank2AccountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            return false;
        }

        //增加金额
        int i = bank2AccountInfoDao.updateAccountBalance(accountChangeEvent.getToAccountNo(), accountChangeEvent.getAmount());
        //添加事务记录，用于幂等
        int j = bank2AccountInfoDao.addTx(accountChangeEvent.getTxNo());

        if (i == 1 && j == 1) {
            return true;
        }

        if (accountChangeEvent.getAmount() == 4) { // TODO: 2024/7/30 测试代码，去掉
            throw new RuntimeException("人为制造异常");
        }

        throw new RuntimeException("账户金额更新失败");
    }
}

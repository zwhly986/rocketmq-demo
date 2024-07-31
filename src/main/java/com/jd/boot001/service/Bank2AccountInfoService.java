package com.jd.boot001.service;

import com.jd.boot001.entity.AccountChangeEvent;

public interface Bank2AccountInfoService {

    //更新账户，增加金额
    boolean addAccountInfoBalance(AccountChangeEvent accountChangeEvent);

}

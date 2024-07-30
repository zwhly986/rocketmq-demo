package com.jd.boot001.entity;

import lombok.Data;

/**
 * 账户变更事件，
 * RocketMQ用事务消息测试
 */
@Data
public class AccountChangeEvent {

    private String accountNo;
    private Double amount;
    private String txNo;

    public AccountChangeEvent() {
    }

    public AccountChangeEvent(String accountNo, Double amount, String txNo) {
        this.accountNo = accountNo;
        this.amount = amount;
        this.txNo = txNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTxNo() {
        return txNo;
    }

    public void setTxNo(String txNo) {
        this.txNo = txNo;
    }

}

package com.jd.boot001.entity;

import lombok.Data;

/**
 * 账户变更事件，
 * RocketMQ用事务消息测试
 */
@Data
public class AccountChangeEvent {

    /** 转出账户 */
    private String fromAccountNo;
    /** 转入账户 */
    private String toAccountNo;
    /** 转账金额 */
    private Double amount;
    /** 事务消息编号 */
    private String txNo;

    public AccountChangeEvent() {
    }

    public AccountChangeEvent(String fromAccountNo, String toAccountNo, Double amount, String txNo) {
        this.fromAccountNo = fromAccountNo;
        this.toAccountNo = toAccountNo;
        this.amount = amount;
        this.txNo = txNo;
    }

    public String getFromAccountNo() {
        return fromAccountNo;
    }

    public void setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    public String getToAccountNo() {
        return toAccountNo;
    }

    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
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

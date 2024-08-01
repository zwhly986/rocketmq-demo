package com.jd.boot001.entity;

/**
 * 订单
 */
public class Order {
    //订单号
    private String orderId;
    //订单的流程名称
    private String flowName;
    //订单的流程顺序
    private String seq;

    public Order() {
    }

    public Order(String orderId, String flowName, String seq) {
        this.orderId = orderId;
        this.flowName = flowName;
        this.seq = seq;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", flowName='" + flowName + '\'' +
                ", seq='" + seq + '\'' +
                '}';
    }
}

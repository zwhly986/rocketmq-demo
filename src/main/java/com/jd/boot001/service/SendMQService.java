package com.jd.boot001.service;

import com.jd.boot001.common.R;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * RocketMQ消息发送
 */
public interface SendMQService {

    void sync();

    void async();

    /**
     * 延迟消息发送
     *
     * @param msg
     * @return
     * @throws MQClientException
     */
    R sendDelayMsg(String msg) throws Exception;

}


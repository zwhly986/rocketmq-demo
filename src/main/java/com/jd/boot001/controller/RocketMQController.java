package com.jd.boot001.controller;

import com.jd.boot001.common.R;
import com.jd.boot001.utils.SendOrderlyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RocketMQ测试（For RocketMQ5.2.0）
 * 顺序消息发送（同步和异步）
 */
@RestController
@RequestMapping("/rocketMQ")
public class RocketMQController {

    @Autowired
    private SendOrderlyMessage sendOrderlyMessage;

    /**
     * 同步发送消息
     */
    @GetMapping("/sync")
    public R sync() {
        sendOrderlyMessage.sync();
        return R.success();
    }

    /**
     * 异步发送消息
     */
    @GetMapping("/async")
    public R async() {
        sendOrderlyMessage.async();
        return R.success();
    }

}

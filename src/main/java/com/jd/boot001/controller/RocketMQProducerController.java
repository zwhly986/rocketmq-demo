package com.jd.boot001.controller;

import com.alibaba.fastjson2.JSON;
import com.jd.boot001.common.R;
import com.jd.boot001.entity.Order;
import com.jd.boot001.service.SendMQService;
import com.jd.boot001.utils.DateUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RocketMQ消息发送 (For RocketMQ5.2.0)
 * 1.顺序消息发送（同步、异步、oneWay）
 * 2.延迟消息
 * 3.顺序消息
 * 4.批量消息发送
 */
@RestController
@RequestMapping("/rocketMQProducer")
public class RocketMQProducerController {
    private static final Logger log = LoggerFactory.getLogger(RocketMQProducerController.class);

    private static final AtomicLong atomicLong = new AtomicLong(0);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private SendMQService sendMQService;

    /**
     * 同步发送消息
     * http://localhost:8080/boot001/rocketMQProducer/sync
     */
    @GetMapping("/sync")
    public R sync() {
        sendMQService.sync();
        return R.success();
    }

    /**
     * 异步发送消息
     * http://localhost:8080/boot001/rocketMQProducer/async
     */
    @GetMapping("/async")
    public R async() {
        sendMQService.async();
        return R.success();
    }

    /********************************************************/


    /**
     * Tag过滤：主题相同的情况下，如果消费者订阅的Tag和发送者设置的消息Tag相互匹配，则消息被投递给消费端进行消费。
     * http://localhost:8080/boot001/rocketMQProducer/send/tag
     * 监听器：TagTopicMsgListener
     *
     * @return
     */
    @GetMapping("/send/tag")
    public String sendSyncMessage() {

        /**
         * 发送一个带 Tag 的同步消息
         *
         * 消息主题：tagTopic
         * 消息标签Tag：java
         * 参数一：topic，如果想添加tag，可以使用"topic:tag"写法
         * 参数二：消息内容
         * 消息主题相同，且消费者订阅的Tag和发送者设置的消息Tag相互匹配，则消息被投递给消费端进行消费
         * topic如果想加标签，写法是topic:tag
         */
        SendResult result = rocketMQTemplate.syncSend("tagTopic:java", "这是一个带有java tag的消息[" + DateUtils.date() + "]");
        return "带标签的消息发送状态：" + result.getSendStatus() + "<br>消息id：" + result.getMsgId();
    }


    /**
     * 发送同步消息
     * http://localhost:8080/boot001/rocketMQProducer/send/sync/同步消息001
     * <p>
     * 监听器：SyncMsgListener
     *
     * @param msg
     * @return
     */
    @GetMapping("/send/sync/{msg}")
    public String sendSyncMessage(@PathVariable String msg) {
        SendResult result = rocketMQTemplate.syncSend("syncTopic", msg);
        return "同步消息发送状态：" + result.getSendStatus() + "<br>消息id：" + result.getMsgId();
    }


    /**
     * 发送异步消息
     * http://localhost:8080/boot001/rocketMQProducer/send/async/异步消息002
     * 监听器：AsyncMsgListener
     *
     * @param msg
     * @return
     */
    @GetMapping("/send/async/{msg}")
    public String sendAsyncMessage(@PathVariable String msg) {
        rocketMQTemplate.asyncSend("asyncTopic", msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("异步消息发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("异步消息发送失败", throwable);
            }
        });

        log.info("异步消息已发送完成");
        return "发送异步消息";
    }


    /**
     * 发送单向信息
     * http://localhost:8080/boot001/rocketMQProducer/send/oneWay/单向消息003
     * 监听器：OneWayMsgListener
     *
     * @param msg
     * @return
     */
    @GetMapping("/send/oneWay/{msg}")
    public String sendOneWayMessage(@PathVariable String msg) {
        rocketMQTemplate.sendOneWay("oneWayTopic", msg);
        return "单向消息发送成功";
    }


    /**
     * // TODO: 2024/8/1 待解决，如何设定延迟时间为任意值（实际需求）
     * 延迟消息发送
     * http://localhost:8080/boot001/rocketMQProducer/send/delay/延迟消息004
     *
     * @param msg
     * @return
     */
    @GetMapping("/send/delay/{msg}")
    public String sendDelayMessage(@PathVariable String msg) throws Exception {
        long num = atomicLong.getAndIncrement();
        String msgx = String.format("%s[%d][发送时间：%s]", msg, num, DateUtils.date());
        Message<String> message = MessageBuilder.withPayload(msgx).build();

        /*
        参数四：延迟级别 "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
        设置延迟消费时间，设置延迟时间级别0,18,0表示不延迟，18表示延迟2h，大于18的都是2h
        */
        // 1.同步发送
        SendResult result = rocketMQTemplate.syncSend("delayTopic", message, 2000, 18);
        return "延迟消息发送状态：" + result.getSendStatus() + "<br>消息id：" + result.getMsgId() + "<br>消息发送时间：" + DateUtils.date();


        /*
        // 2.异步发送
        // 延迟级别 "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
        rocketMQTemplate.asyncSend("delayTopic", message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("延迟消息异步发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("延迟消息异步发送失败");
            }
        }, 2000, 3);

        return "延迟消息异步发送完成：" + DateUtils.date();
        */

        /*// 3.延迟时间任意（定时） // TODO: 2024/8/2
        R result = sendMQService.sendDelayMsg(msg);
        return JSON.toJSONString(result);*/

    }


    /**
     * 发送顺序消息
     * http://localhost:8080/boot001/rocketMQProducer/send/orderly
     * 监听器：OrderMsgListener
     * 重载方法：
     * syncSendOrderly 发送同步顺序消息
     * asyncSendOrderly 发送异步同步消息
     * sendOneWayOrderly 发送单向顺序消息
     *
     * @return
     */
    @GetMapping("/send/orderly")
    public String sendOrderlyMessage() {
        String orderId = UUID.randomUUID().toString();
        String orderId2 = UUID.randomUUID().toString();

        List<Order> orders = Arrays.asList(
                new Order(orderId, "下订单", "1"),
                new Order(orderId, "发短信", "2"),
                new Order(orderId, "物流", "3"),
                new Order(orderId, "签收", "4"),

                new Order(orderId2, "下订单", "1"),
                new Order(orderId2, "发短信", "2"),
                new Order(orderId2, "物流", "3"),
                new Order(orderId2, "签收", "4")
        );

        //控制流程：下订单->发短信->物流->签收
        //将 orderId 作为 hashKey，这样 orderId 相同的会放在同一个队列里面，顺序消费 // TODO: 2024/8/1 超重点（一个主题对应多个队列，保证数据顺序消费且消息不积压）
        orders.forEach(order -> {
            // 参数一：topic，如果想添加tag，可以使用"topic:tag"写法
            // 参数二：消息内容
            // 参数三：hashkey，使用此参数选择队列
            rocketMQTemplate.syncSendOrderly("orderlyMsgTopic", order, order.getOrderId());
        });
        return "顺序消息发送成功";
    }

    /**
     * 发送批量消息
     * http://localhost:8080/boot001/rocketMQProducer/send/batch
     *
     * @return
     */
    @GetMapping("/send/batch")
    public String sendBatchMessage() {
        List<Message> messages = Arrays.asList(
                MessageBuilder.withPayload(new Order(UUID.randomUUID().toString(), "下订单", "1")).build(),
                MessageBuilder.withPayload(new Order(UUID.randomUUID().toString(), "下订单", "1")).build(),
                MessageBuilder.withPayload(new Order(UUID.randomUUID().toString(), "下订单", "1")).build(),
                MessageBuilder.withPayload(new Order(UUID.randomUUID().toString(), "下订单", "1")).build()
        );

        SendResult result = rocketMQTemplate.syncSend("batchOrderly", messages);
        log.info("批量消息发送结果：{}", result);

        return "批量消息发送结果：" + result.getSendStatus().toString();
    }

    /**
     * 发送集合消息
     * http://localhost:8080/boot001/rocketMQProducer/send/list
     *
     * @return
     */
    @GetMapping("/send/list")
    public String sendListMessage() {

        List<Order> orders = Arrays.asList(
                new Order(UUID.randomUUID().toString(), "下订单", "1"),
                new Order(UUID.randomUUID().toString(), "下订单", "1"),
                new Order(UUID.randomUUID().toString(), "下订单", "1"),
                new Order(UUID.randomUUID().toString(), "下订单", "1")
        );

        SendResult result = rocketMQTemplate.syncSend("listTopic", orders);
        log.info("集合消息发送结果：{}", result);
        return "集合消息发送结果：" + result.getSendStatus().toString();
    }


}

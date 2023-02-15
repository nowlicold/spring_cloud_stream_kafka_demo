package com.example.kafka_demo;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author cold
 * @date 2023/2/15 17:05
 */
@RestController
public class SendMqController {
    @Resource
    private MsgPushMessageChannel msgPushMessageChannel;
    @RequestMapping("/sendMq")
    public String sendMq(){
        System.out.println("发送消息");
        KafkaDemoPayload kafkaDemoPayload = new KafkaDemoPayload();
        kafkaDemoPayload.setContent("test");
        msgPushMessageChannel.sendKafkaDemo().send(MessageBuilder.withPayload(kafkaDemoPayload).build());
        return "ok";

    }
}

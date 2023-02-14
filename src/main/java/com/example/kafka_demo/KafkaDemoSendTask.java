package com.example.kafka_demo;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author cold
 * @date 2023/2/14 19:07
 */
@Component
public class KafkaDemoSendTask {
    @Resource
    private MsgPushMessageChannel msgPushMessageChannel;
    @Scheduled(cron = "0/10 * * * * *")
    void cronSchedule() {
        System.out.println("发送消息");
        KafkaDemoPayload kafkaDemoPayload = new KafkaDemoPayload();
        kafkaDemoPayload.setContent("test");
        msgPushMessageChannel.sendKafkaDemo().send(MessageBuilder.withPayload(kafkaDemoPayload).build());
    }
}

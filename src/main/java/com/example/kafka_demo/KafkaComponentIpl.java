package com.example.kafka_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author cold
 * @date 2023/2/14 22:12
 */
@Service
public class KafkaComponentIpl {
    @Autowired
    private MsgPushMessageChannel msgPushMessageChannel;

    public void push(){
        KafkaDemoPayload kafkaDemoPayload = new KafkaDemoPayload();
        kafkaDemoPayload.setContent("test");
        msgPushMessageChannel.sendKafkaDemo().send(MessageBuilder.withPayload(kafkaDemoPayload).build());
    }
}

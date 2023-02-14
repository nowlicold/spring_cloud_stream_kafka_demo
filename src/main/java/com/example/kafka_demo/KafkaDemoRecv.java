package com.example.kafka_demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author cold
 * @date 2023/2/14 19:26
 */
@Slf4j
@Component
public class KafkaDemoRecv {
    @StreamListener(KafkaChannel.PUBLIC_KAFKA_DEMO)
    public void meetJoin(Message<KafkaDemoPayload> message) {
        System.out.println("接收消息：content={}"+message);
    }
}

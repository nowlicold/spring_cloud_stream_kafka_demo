package com.example.kafka_demo;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author cold
 * @date 2023/2/14 19:03
 */
public interface MsgPushMessageChannel {

    @Input(KafkaChannel.PUBLIC_KAFKA_DEMO)
    MessageChannel recvKafkaDemo();

    // 用户被移出会议
    @Output(KafkaChannel.KAFKA_DEMO)
    MessageChannel sendKafkaDemo();
}

package com.cloud.common.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 消费者（监听topic队列）
 * @author liulei
 */
@Component
public class ConsumerListener {

    @KafkaListener(topicPattern = "${spring.kafka.producer.myTopic}")
    public void listen(ConsumerRecord<?,String> record) {
        System.out.println(record);
    }
}

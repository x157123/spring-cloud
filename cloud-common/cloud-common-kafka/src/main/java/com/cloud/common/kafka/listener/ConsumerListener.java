package com.cloud.common.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消费者（监听topic队列）
 *
 * @author liulei
 */
@Component
public class ConsumerListener {

    @KafkaListener(topicPattern = "${spring.kafka.topic.myTopic}")
    public void listen(ConsumerRecord<?, String> record) {
        System.out.println(record);
    }



    @KafkaListener(topics = "${spring.kafka.topic.myTopics}")
    public void locationHistory(List<ConsumerRecord<String, String>> messages, Acknowledgment ack) {
        try {
            for (ConsumerRecord<String, String> message : messages) {
                System.out.println(message.value());
            }
        } finally {
            ack.acknowledge();
        }
    }
}


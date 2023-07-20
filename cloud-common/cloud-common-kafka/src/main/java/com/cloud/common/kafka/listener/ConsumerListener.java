package com.cloud.common.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
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
    public void listen(List<ConsumerRecord<String, String>> messages) {
        StringBuffer stringBuffer = new StringBuffer();
        for (ConsumerRecord<String, String> message : messages) {
            stringBuffer.append(message.value()).append(":");
        }
        System.out.println(stringBuffer.toString());
    }


    @KafkaListener(topics = "${spring.kafka.topic.myTopics}")
    public void locationHistory(List<ConsumerRecord<String, String>> messages) {
        for (ConsumerRecord<String, String> message : messages) {
            System.out.println(message.value());
        }
    }
}


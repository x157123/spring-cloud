package com.cloud.sync.listener;

import com.alibaba.fastjson2.JSONObject;
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

    @KafkaListener(topicPattern = "${spring.kafka.topic.debeziumTopic}")
    public void listen(List<ConsumerRecord<String, String>> messages) {
        for (ConsumerRecord<String, String> message : messages) {

            JSONObject jsonObject = JSONObject.parseObject(message.value());
            String op = jsonObject.getString("op");
            if (op != null) {
                switch (op) {
                    case "r":
                        System.out.print(message.topic() + "读取数据:->" + jsonObject.get("after"));
                        break;
                    case "c":
                        System.out.print(message.topic() + "创建数据:->" + jsonObject.get("after"));
                        break;
                    case "u":
                        System.out.print(message.topic() + "更新数据:->" + jsonObject.get("after"));
                        break;
                    case "d":
                        System.out.print(message.topic() + "删除数据:->" + jsonObject.get("before"));
                        break;
                }
            }
        }
    }
}


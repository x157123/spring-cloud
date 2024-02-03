package com.cloud.sync.read.listener;

import com.alibaba.fastjson2.JSONObject;
import com.cloud.sync.read.service.SyncService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消费者（监听topic队列）
 *
 * @author liulei
 */
@Component
public class ConsumerListener {

    private SyncService syncService;

    public ConsumerListener(SyncService syncService) {
        this.syncService = syncService;
    }

    @KafkaListener(topicPattern = "${spring.kafka.topic.debeziumTopic}")
    public void listen(List<ConsumerRecord<String, String>> messages) {
        Map<String, List<String>> map = new HashMap<>();
        for (ConsumerRecord<String, String> message : messages) {
            JSONObject jsonObject = JSONObject.parseObject(message.value());
            if (jsonObject == null) {
                continue;
            }
            System.out.println("kafka消费打印数据:-->" + jsonObject);
            String op = jsonObject.getString("op");
            if (op != null) {
                switch (op) {
                    case "r":
                    case "c":
                    case "u":
                        putData(map, "c." + message.topic(), jsonObject.get("after").toString());
                        break;
                    case "d":
//                        putData(map, op + "." + message.topic(), jsonObject.get("before").toString());
                        break;
                }
            }
        }
        syncService.writeData(map);
    }

    private void putData(Map<String, List<String>> map, String key, String data) {
        List<String> list = map.get(key);
        if (list == null) {
            list = new ArrayList<>();
            map.put(key, list);
        }
        list.add(data);
    }
}


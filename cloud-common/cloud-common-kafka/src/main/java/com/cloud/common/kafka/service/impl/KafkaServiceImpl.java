package com.cloud.common.kafka.service.impl;

import com.cloud.common.kafka.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * KafkaService实现类
 * @author liulei
 */
@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
 
    @Override
    public void send(String topic, String value) {
        CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(topic, value);
    }
}

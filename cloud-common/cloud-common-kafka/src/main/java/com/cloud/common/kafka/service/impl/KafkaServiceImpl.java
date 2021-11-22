package com.cloud.common.kafka.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import com.cloud.common.kafka.service.KafkaService;

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
        ListenableFuture<SendResult<String, String>> resultListenableFuture = kafkaTemplate.send(topic, value);
        resultListenableFuture.addCallback(
                successCallback -> log.info("发送成功：topic= " + topic + " value= " + value),
                failureCallback -> log.info("发送失败：topic= " + topic + " value= " + value));
    }
}

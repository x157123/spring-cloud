package com.cloud.common.kafka.service;

/**
 * KafkaService接口类
 * @author liulei
 */
public interface KafkaService {

    /**
     * 发送消息
     * @param topic 消息主题
     * @param value 消息内容
     */
    public void send(String topic, String value);

}

package com.cloud.common.kafka.controller;

import com.cloud.common.kafka.config.KafkaConfiguration;
import com.cloud.common.kafka.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liulei
 */
@Slf4j
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    /**
     * 发送文本消息
     * @param msg
     * @return
     */
    @GetMapping("/send/{msg}")
    public String send(@PathVariable String msg) {
//        kafkaService.send(kafkaConfiguration.getT(), msg);
        return "生产者发送消息给topic1："+msg;
    }

}

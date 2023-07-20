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
     *
     * @param msg
     * @return
     */
    @GetMapping("/send/{msg}")
    public String send(@PathVariable String msg) {
        for (int i = 0; i < 1000; i++) {
            kafkaService.send("topic-" + (i % 3), "topic-" + (i % 3));
        }
        return "成功";
    }

}

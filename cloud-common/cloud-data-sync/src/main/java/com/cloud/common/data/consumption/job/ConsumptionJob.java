package com.cloud.common.data.consumption.job;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author liulei
 */
public class ConsumptionJob {


    /**
     * 写入数据库
     */
    @Scheduled(fixedDelay = 300)
    private void insertData() {

    }

    /**
     * 每隔十秒检测一次啊数据库
     */
    @Scheduled(fixedDelay = 10000)
    private void detectMerge(){

    }
}

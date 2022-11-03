package com.cloud.sync.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Component
public class SyncService {

    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("pool-%d").build();

    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory);

    Map<String, DebeziumEngine<ChangeEvent<String, String>>> map = new HashMap<>();


    public void begin() {
        DebeziumEngine<ChangeEvent<String, String>> engine = Sync.getMysql();
        executor.execute(engine);
        map.put("mysql", engine);
    }


    public void start() {
        DebeziumEngine<ChangeEvent<String, String>> mysql = map.get("mysql");
        executor.execute(mysql);
    }


    public void stop() {
        try {
            DebeziumEngine<ChangeEvent<String, String>> mysql = map.get("mysql");
            mysql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void msg() {

        int queueSize = executor.getQueue().size();
        System.out.println("当前排队线程数：" + queueSize);

        int activeCount = executor.getActiveCount();
        System.out.println("当前活动线程数：" + activeCount);

        long completedTaskCount = executor.getCompletedTaskCount();
        System.out.println("执行完成线程数：" + completedTaskCount);

        long taskCount = executor.getTaskCount();
        System.out.println("总线程数：" + taskCount);
    }

}

package com.cloud.common.data.consumption.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author liulei
 */
public class Tests {

    public static void main(String[] args) throws IOException {
        // Define the configuration for the Debezium Engine with MySQL connector...
        final Properties props = new Properties();
        props.setProperty("name", "engine");

        props.setProperty("connector.class", MySqlConnector.class.getCanonicalName());

        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "E:/offsets.dat");
        props.setProperty("offset.flush.interval.ms", "60000");
        /* begin connector properties */
        props.setProperty("database.hostname", "127.0.0.1");
        props.setProperty("database.port", "3306");
        props.setProperty("database.user", "root");
        props.setProperty("database.password", "123456");

        props.setProperty("database.serverTimezone", "UTC");

        props.setProperty("table.whitelist", "test.issue_focus_tag");

        props.setProperty("database.server.id", "1");
        props.setProperty("database.server.name", "my-app-connector");
        props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
        props.setProperty("database.history.file.filename", "E:/dbhistory.dat");

        // Create the engine with this configuration ...

        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(props)
                .notifying(record -> {
                    System.out.println("record.key() = " + record.key());
                    System.out.println("record.value() = " + record.value());
                }).using((success, message, error) -> {
                    // 强烈建议加上此部分的回调代码，方便查看错误信息
                    if (!success && error != null) {
                        // 报错回调
                        System.out.println("----------error------");
                        System.out.println("error:" + message);
                    }
                }).build();

//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(engine);


        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("pool-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), threadFactory);

        executor.execute(engine);

        System.out.println("我要关闭了1");

//        engine.close();
    }

}

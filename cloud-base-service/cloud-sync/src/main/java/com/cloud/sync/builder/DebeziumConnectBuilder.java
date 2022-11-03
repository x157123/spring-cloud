package com.cloud.sync.builder;

import com.cloud.sync.storage.JdbcOffsetBackingStore;
import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

import java.util.Properties;

public class DebeziumConnectBuilder {

    public static DebeziumEngine<ChangeEvent<String, String>> getMysql() {

        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(getProperties())
                .notifying(record -> {
                    System.out.println("record.value() = " + record.value());
                }).using((success, message, error) -> {
                    // 强烈建议加上此部分的回调代码，方便查看错误信息
                    if (!success && error != null) {
                        // 报错回调
                        System.out.println("----------error------");
                        System.out.println("error:" + message);
                    }
                }).build();

        return engine;
    }


    private static Properties getProperties() {
        final Properties props = new Properties();
        props.setProperty("name", "engine");

        props.setProperty("connector.class", MySqlConnector.class.getCanonicalName());
        /* 使用数据库存储 */
        props.setProperty("offset.storage", JdbcOffsetBackingStore.class.getCanonicalName());

        props.setProperty("offset.flush.interval.ms", "60000");
        /* begin connector properties */
        props.setProperty("database.hostname", "127.0.0.1");
        props.setProperty("database.port", "3306");
        props.setProperty("database.user", "root");
        props.setProperty("database.password", "123456");

        props.setProperty("database.serverTimezone", "UTC");

        props.setProperty("table.whitelist", "test.sg_sync_diversified,test.sg_sync_person");

        props.setProperty("database.server.id", "1");
        props.setProperty("database.server.name", "my-app-connector");
        props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
        props.setProperty("database.history.file.filename", "D:/dbhistory.dat");
        return props;
    }
}

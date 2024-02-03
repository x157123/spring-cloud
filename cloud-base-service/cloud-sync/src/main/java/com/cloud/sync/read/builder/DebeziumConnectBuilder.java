package com.cloud.sync.read.builder;

import com.cloud.sync.read.storage.JdbcOffsetBackingStore;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

import java.util.Properties;

public class DebeziumConnectBuilder {

    /**
     * https://debezium.io/documentation/reference/2.0/development/engine.html
     * doc
     *
     * @return
     */
    public static DebeziumEngine<ChangeEvent<String, String>> getMysql() {

        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class).using(getProperties()).notifying(record -> {
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
        /* 使用数据库存储 */
        props.setProperty("offset.storage", JdbcOffsetBackingStore.class.getCanonicalName());

        props.setProperty("offset.flush.interval.ms", "60000");
        /* begin connector properties */
        props.setProperty("topic.prefix", "my-app-connector");
        props.setProperty("database.server.id", "85744");
        props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        props.setProperty("database.hostname", "127.0.0.1");
        props.setProperty("database.port", "3306");
        props.setProperty("database.user", "root");
        props.setProperty("database.password", "123456");
        props.setProperty("database.connectionTimeZone", "Asia/Shanghai");

        /** 采集表配置 */
        props.setProperty("database.include.list", "test");
        props.setProperty("table.include.list", "test.gp_area_info");


        /** 去除 json schema 数据*/
        props.setProperty("key.converter", "org.apache.kafka.connect.json.JsonConverter");
        props.setProperty("value.converter", "org.apache.kafka.connect.json.JsonConverter");
        props.setProperty("key.converter.schemas.enable", "false");
        props.setProperty("value.converter.schemas.enable", "false");


        /** 采集表配置 */
        props.setProperty("table.include.list", "test.gp_area_info");

        props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
        props.setProperty("schema.history.internal.file.filename", "D:/dbhistory.dat");
        return props;
    }
}

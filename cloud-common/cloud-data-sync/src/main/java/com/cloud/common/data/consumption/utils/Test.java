package com.cloud.common.data.consumption.utils;

import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.debezium.relational.history.FileDatabaseHistory;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liulei
 */
public class Test {

    public static void main(String[] args) {
        // 0. 配置数据库，添加用户，赋予主从同步的权限

        // 1. 生成配置
        Properties props = genProps();

        // 2. 构建 DebeziumEngine
        // 使用 Json 格式
        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(props)
                .notifying(record -> {
                    // record中会有操作的类型（增、删、改）和具体的数据
                    // key是主键
                    System.out.println("record.key() = " + record.key());
                    System.out.println("record.value() = " + record.value());
                })
                .using((success, message, error) -> {
                    // 强烈建议加上此部分的回调代码，方便查看错误信息

                    if (!success && error != null) {
                        // 报错回调
                        System.out.println("----------error------");
                        System.out.println("error:" + message);
                    }
                }).build();

        // 3. 正式运行
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);
    }

    private static Properties genProps() {
        // 配置
        Properties props = new Properties();
        // 在maven处引入其他数据库的连接器，例如debezium-connector-postgres，再修改此处的connector.class，即可使用其他数据库的CDC
        props.setProperty("connector.class", MySqlConnector.class.getCanonicalName());
        // 可以任意修改
        props.setProperty("database.server.name", "my_server_01");
        // IP
        props.setProperty("database.hostname", "127.0.0.1");
        // 端口
        props.setProperty("database.port", String.valueOf(3306));
        // 用户
        props.setProperty("database.user", "cdc_user");
        // 密码
        props.setProperty("database.password", "cdc_password");
        // 时区
        props.setProperty("database.serverTimezone", "UTC");
        // 下面两个是数据库和表，注意只能选择一种:
        // 1. 使用database.whitelist，只设置数据库（会通知全库的CDC信息）
        // 2. 使用table.whitelist，设置库名和表名（会通知单个库的单个表的CDC信息）
//        props.setProperty("database.whitelist", "db_inventory_cdc");
        props.setProperty("table.whitelist", "test.wgh_person_hj"); // 库.表名

        props.setProperty("name", "engine");
//      props.setProperty("offset.storage", FileOffsetBackingStore.class.getCanonicalName());
//      props.setProperty("offset.storage.file.filename", "./storage/offset.dat");
//      props.setProperty("offset.flush.interval.ms", String.valueOf(60000L));
        props.setProperty("key.converter.schemas.enable", "false");
        props.setProperty("value.converter.schemas.enable", "false");
        props.setProperty("include.schema.changes", "false");
        props.setProperty("tombstones.on.delete", "false");
        props.setProperty("database.history", FileDatabaseHistory.class.getCanonicalName());
        props.setProperty("database.history.store.only.monitored.tables.ddl", "true");
        props.setProperty("database.history.file.filename", "./storage/dbhistory.dat");
        props.setProperty("database.history.instance.name", UUID.randomUUID().toString());
        props.setProperty("database.history.skip.unparseable.ddl", "true");
        return props;
    }

}

package com.cloud.sync.service.impl;

import com.cloud.sync.builder.DateTimeConverter;
import com.cloud.sync.service.ConnectConfigService;
import com.cloud.sync.service.SyncService;
import com.cloud.sync.storage.JdbcOffsetBackingStore;
import com.cloud.sync.vo.ConnectConfigVo;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Component
public class SyncServiceImpl implements SyncService {

    private final ConnectConfigService connectConfigService;

    private KafkaTemplate<String, String> kafkaTemplate;

    public SyncServiceImpl(ConnectConfigService connectConfigService, KafkaTemplate<String, String> kafkaTemplate) {
        this.connectConfigService = connectConfigService;
        this.kafkaTemplate = kafkaTemplate;
    }

    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("pool-%d").build();

    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory);

    Map<String, DebeziumEngine<ChangeEvent<String, String>>> map = new HashMap<>();


    @Override
    public void begin(Long connectId) {
        ConnectConfigVo connectConfigVo = connectConfigService.findById(connectId);
        DebeziumEngine<ChangeEvent<String, String>> engine = getDebeziumEngine(connectConfigVo);
        executor.execute(engine);
        map.put("debezium_" + connectConfigVo.getId().toString(), engine);
    }


    @Override
    public void start(Long connectId) {
        ConnectConfigVo connectConfigVo = connectConfigService.findById(connectId);
        DebeziumEngine<ChangeEvent<String, String>> mysql = map.get("debezium_" + connectConfigVo.getId().toString());
        executor.execute(mysql);
    }


    @Override
    public void stop(Long connectId) {
        ConnectConfigVo connectConfigVo = connectConfigService.findById(connectId);
        try {
            DebeziumEngine<ChangeEvent<String, String>> mysql = map.get("debezium_" + connectConfigVo.getId().toString());
            mysql.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
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

    @Override
    public void writeData(Map<String, List<String>> map) {
        if (map != null && map.size() > 0) {
            map.keySet();
            for (String key : map.keySet()) {
                System.out.println(key);
            }
            //读取表结构信息

            //调用插入语句
        }
    }


    private DebeziumEngine<ChangeEvent<String, String>> getDebeziumEngine(ConnectConfigVo connectConfigVo) {
        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class).using(getProperties(connectConfigVo)).notifying(record -> {
//            System.out.println("record.value() =》 " + record.destination());
//            System.out.println("record.value() =》 " + record.value());
            kafkaTemplate.send(record.destination(), record.value());
        }).using((success, message, error) -> {
            // 强烈建议加上此部分的回调代码，方便查看错误信息
            if (!success && error != null) {
                // 报错回调
                System.out.println("----------同步异常----------");
                System.out.println("error:" + message);
            }
        }).build();
        return engine;
    }


    private Properties getProperties(ConnectConfigVo connectConfigVo) {
        final Properties props = getProperties();
        props.setProperty("topic.prefix", "debezium_" + connectConfigVo.getId().toString());
        props.setProperty("database.server.id", connectConfigVo.getId().toString());
        if (connectConfigVo.getType() == 1) {
            props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        }
        props.setProperty("database.hostname", connectConfigVo.getHostname());
        props.setProperty("database.port", connectConfigVo.getPort().toString());
        props.setProperty("database.user", connectConfigVo.getUser());
        props.setProperty("database.password", connectConfigVo.getPassword());

        /** 采集表配置 */
        props.setProperty("table.include.list", "test.gp_area_info");
        return props;
    }

    private static Properties getProperties() {
        final Properties props = new Properties();

        props.setProperty("name", "engine");

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


        //日期时间类型转换器自定义属性（属性前缀为转换器名称）
        props.setProperty("converters", DateTimeConverter.CONVERTERS_NAME);
        props.setProperty(DateTimeConverter.CONVERTERS_TYPE, DateTimeConverter.class.getCanonicalName());

        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_TIME, "HH:mm:ss");
        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_DATE, "yyyy-MM-dd");
        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_DATETIME, "yyyy-MM-dd HH:mm:ss");
        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_TIMESTAMP, "yyyy-MM-dd HH:mm:ss");
        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_TIMESTAMP_ZONID, "Asia/Shanghai");

        /** 采集表配置 */
        props.setProperty("database.include.list", "test");
        props.setProperty("table.include.list", "test.gp_area_info");
        //  对于decimal类型数据，在默认精度下会显示为"F3A="，通过设置属性decimal.handling.mode为double或string即可解决
        props.setProperty("decimal.handling.mode", "string");
        //  默认情况下，连接器在每次启动时都会首先获取一个全局读锁，然后进行快照读取。 首先获取全局读锁是不推荐的，可以设置snapshot.locking.mode=none使用行锁来代替
        props.setProperty("snapshot.locking.mode", "none");

        /** 去除 json schema 数据*/
        props.setProperty("key.converter", "org.apache.kafka.connect.json.JsonConverter");
        props.setProperty("value.converter", "org.apache.kafka.connect.json.JsonConverter");
        props.setProperty("key.converter.schemas.enable", "false");
        props.setProperty("value.converter.schemas.enable", "false");


        /** 采集表配置 */
        props.setProperty("table.include.list", "test.gp_area_info");

        /** 使用文件存储数据库模式历史 */
        props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
        props.setProperty("schema.history.internal.file.filename", "D:/dbhistory.dat");

        //使用kafka存储数据库模式历史
//        props.setProperty("database.history", KafkaDatabaseHistory.class.getCanonicalName());
//        props.setProperty("database.history.kafka.topic", "debezium.database.history");
//        props.setProperty("database.history.kafka.bootstrap.servers", "kafka-node:9092");


        /* 使用数据库存储移量 */
        props.setProperty("offset.storage", JdbcOffsetBackingStore.class.getCanonicalName());

        //使用kafka存储偏移量
//        props.setProperty("bootstrap.servers", "kafka-node:9092");
//        props.setProperty("offset.storage", KafkaOffsetBackingStore.class.getCanonicalName());
//        props.setProperty("offset.storage.topic", "debezium.offset.storage");
//        props.setProperty("offset.storage.partitions", "1");
//        props.setProperty("offset.storage.replication.factor", "1");

        return props;
    }
}

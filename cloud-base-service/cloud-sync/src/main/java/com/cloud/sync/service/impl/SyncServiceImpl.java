package com.cloud.sync.service.impl;

import com.cloud.sync.builder.DateTimeConverter;
import com.cloud.sync.service.*;
import com.cloud.sync.storage.JdbcOffsetBackingStore;
import com.cloud.sync.vo.ConnectConfigVo;
import com.cloud.sync.vo.ServeVo;
import com.cloud.sync.vo.TableConfigVo;
import com.cloud.sync.writer.CommonWriter;
import com.cloud.sync.writer.DataBaseType;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Component
public class SyncServiceImpl implements SyncService {

    private final ServeService serviceService;

    private final TableConfigService tableConfigService;

    private final ConnectConfigService connectConfigService;

    private final ServeConfigService serveConfigService;

    private KafkaTemplate<String, String> kafkaTemplate;

    private Map<String, CommonWriter> writerMap = new HashMap<>();

    @Value("${spring.kafka.topic.startTopic}")
    private String debeziumTopic;

    public SyncServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ServeService serviceService
            , TableConfigService tableConfigService, ConnectConfigService connectConfigService, ServeConfigService serveConfigService) {
        this.serviceService = serviceService;
        this.kafkaTemplate = kafkaTemplate;
        this.tableConfigService = tableConfigService;
        this.connectConfigService = connectConfigService;
        this.serveConfigService = serveConfigService;
    }

    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("pool-%d").build();

    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory);

    Map<String, DebeziumEngine<ChangeEvent<String, String>>> map = new HashMap<>();


    @Override
    public void begin(Long serveId) {
        ServeVo serveVo = serviceService.findById(serveId);
        ConnectConfigVo connectConfigVo = connectConfigService.findById(serveVo.getReadConnectId());
        DebeziumEngine<ChangeEvent<String, String>> engine = getDebeziumEngine(serveId, connectConfigVo);
        executor.execute(engine);
        map.put(debeziumTopic + serveId.toString(), engine);
        serveConfigService.state(serveId, 1);
    }


    @Override
    public void stop(Long serveId) {
        try {
            DebeziumEngine<ChangeEvent<String, String>> mysql = map.get(debeziumTopic + serveId.toString());
            mysql.close();
            map.remove(debeziumTopic + serveId);
            serveConfigService.state(serveId, 0);
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
            for (String key : map.keySet()) {
                String keyTmp = key.replace("debezium_", "");
                String[] keys = keyTmp.split("\\.");
                for (String tmp : keys) {
                    System.out.println(tmp);
                }
                System.out.println(map.get(key));
//                //读取表结构信息
//                CommonWriter commonWriter = writerMap.get(key);
//                if (commonWriter == null) {
//                    //调用插入语句
//                    commonWriter = MysqlWriter.getMysqlWriter(1,url,username,password,table,new ArrayList<>());
//                    writerMap.put(key, commonWriter);
//                }
//                try {
//                    commonWriter.writer(new ArrayList<>());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    private DebeziumEngine<ChangeEvent<String, String>> getDebeziumEngine(Long serveId, ConnectConfigVo connectConfigVo) {
        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class).using(getProperties(serveId, connectConfigVo)).notifying(record -> {
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


    private Properties getProperties(Long serveId, ConnectConfigVo connectConfigVo) {
        final Properties props = getProperties();
        props.setProperty("name", debeziumTopic + serveId);
        props.setProperty("topic.prefix", debeziumTopic + serveId);
        props.setProperty("database.server.id", serveId.toString());
        props.setProperty("connector.class", DataBaseType.getDataBaseType(connectConfigVo.getType()).getDebeziumConnector());
        props.setProperty("database.hostname", connectConfigVo.getHostname());
        props.setProperty("database.port", connectConfigVo.getPort().toString());
        props.setProperty("database.user", connectConfigVo.getUser());
        props.setProperty("database.password", connectConfigVo.getPassword());
        /** 采集表配置 */
        props.setProperty("database.include.list", connectConfigVo.getDatabaseName());

        List<TableConfigVo> tableConfigVos = tableConfigService.findByServeId(Arrays.asList(serveId), 1);

        String str = tableConfigVos
                .stream().map(t -> connectConfigVo.getDatabaseName() + "." + t.getTableName())
                .collect(Collectors.joining(","));
        /** 采集表配置 */
        props.setProperty("table.include.list", str);
        return props;
    }

    private static Properties getProperties() {
        final Properties props = new Properties();

        props.setProperty("offset.flush.interval.ms", "60000");
        /* begin connector properties */
        props.setProperty("database.connectionTimeZone", "Asia/Shanghai");

        //日期时间类型转换器自定义属性（属性前缀为转换器名称）
        props.setProperty("converters", DateTimeConverter.CONVERTERS_NAME);
        props.setProperty(DateTimeConverter.CONVERTERS_TYPE, DateTimeConverter.class.getCanonicalName());

        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_TIME, "HH:mm:ss");
        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_DATE, "yyyy-MM-dd");
        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_DATETIME, "yyyy-MM-dd HH:mm:ss");
        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_TIMESTAMP, "yyyy-MM-dd HH:mm:ss");
        props.setProperty(DateTimeConverter.FORMATTER_PATTERN_TIMESTAMP_ZONID, "Asia/Shanghai");

        //  对于decimal类型数据，在默认精度下会显示为"F3A="，通过设置属性decimal.handling.mode为double或string即可解决
        props.setProperty("decimal.handling.mode", "string");
        //  默认情况下，连接器在每次启动时都会首先获取一个全局读锁，然后进行快照读取。 首先获取全局读锁是不推荐的，可以设置snapshot.locking.mode=none使用行锁来代替
        props.setProperty("snapshot.locking.mode", "none");

        /** 去除 json schema 数据*/
        props.setProperty("key.converter", "org.apache.kafka.connect.json.JsonConverter");
        props.setProperty("value.converter", "org.apache.kafka.connect.json.JsonConverter");
        props.setProperty("key.converter.schemas.enable", "false");
        props.setProperty("value.converter.schemas.enable", "false");


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

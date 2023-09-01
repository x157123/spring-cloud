package com.cloud.sync;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.connectors.postgres.PostgreSQLSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Properties;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);
        env.enableCheckpointing(3000);

        env.addSource(pgsqlSource(), "pgsql-source")
                .print().setParallelism(1);


        env.executeAsync("pgsql-source");

        env.fromSource(mysqlSource(), WatermarkStrategy.noWatermarks(), "mysql-source")
                //对接收器使用并行1来保持消息的顺序
                .print().setParallelism(1);

        env.executeAsync("mysql-source");

    }


    private static SourceFunction<String> pgsqlSource() {
        /**
         *
         *  update pg_publication set puballtables=true where pubname is not null;
         *  CREATE PUBLICATION dbz_publication FOR ALL TABLES;
         *
         *  ALTER TABLE newtable REPLICA IDENTITY FULL;
         *  select relreplident from pg_class where relname='newtable';
         *
         */
        SourceFunction<String> sourceFunction = PostgreSQLSource.<String>builder()
                .hostname("home.157123.xyz")
                .port(54320)
                .database("postgres") // set captured database
                .schemaList("public")
                .tableList("public.testtable") // set captured table
                .username("postgres")
                .password("postgres")
                .decodingPluginName("pgoutput")
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                .build();
        return sourceFunction;
    }

    /**
     * 构造变更数据源
     *
     * @return DebeziumSourceFunction<DataChangeInfo>
     */
    private static MySqlSource<String> mysqlSource() {

        Properties properties = new Properties();

        // 好像不起作用使用slot.name
        properties.setProperty("debezium.slot.name", "pg_cdc" + UUID.randomUUID());
        properties.setProperty("slot.name", "flink_slot" + UUID.randomUUID());
        properties.setProperty("debezium.slot.drop.on.top", "true");
        properties.setProperty("slot.drop.on.top", "true");


        return MySqlSource.<String>builder()
                .hostname("127.0.0.1")
                .port(3306)
                .databaseList("test") // set captured database
                .tableList("test.gp_area_info") // set captured table
                .username("root")
                .password("123456")
                // initial:初始化快照,即全量导入后增量导入(检测更新数据写入)
                .startupOptions(StartupOptions.initial())
                .deserializer(new JsonDebeziumDeserializationSchema())
                .debeziumProperties(properties)
                .serverTimeZone("GMT+8")
                .build();
    }

}
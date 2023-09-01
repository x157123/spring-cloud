package com.cloud.sync;

import com.alibaba.fastjson.JSONObject;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;
import java.util.Optional;

/**
 * @desc mysql消息读取自定义序列化
 **/
public class MysqlDeserialization implements DebeziumDeserializationSchema<DbDataInfo> {

    public static final String TS_MS = "ts_ms";
    public static final String BIN_FILE = "file";
    public static final String POS = "pos";
    public static final String CREATE = "CREATE";
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String SOURCE = "source";
    public static final String UPDATE = "UPDATE";

    /**
     * 反序列化数据,转为变更JSON对象
     *
     * @param sourceRecord
     * @param collector
     */
    public void deserialize(SourceRecord sourceRecord, Collector<DbDataInfo> collector) {
        String topic = sourceRecord.topic();
        String[] fields = topic.split("\\.");
        String database = fields[1];
        String tableName = fields[2];
        Struct struct = (Struct) sourceRecord.value();
        final Struct source = struct.getStruct(SOURCE);
        DbDataInfo dbDataInfo = new DbDataInfo();
        // 获取操作类型  CREATE UPDATE DELETE
        Envelope.Operation operation = Envelope.operationFor(sourceRecord);
        String type = operation.toString().toUpperCase();
        OperatorTypeEnum eventType = type.equals(CREATE) ? OperatorTypeEnum.INSERT : UPDATE.equals(type) ? OperatorTypeEnum.UPDATE : OperatorTypeEnum.DELETE;
        dbDataInfo.setBeforeData(getJsonObject(struct, BEFORE).toJSONString());
        dbDataInfo.setAfterData(getJsonObject(struct, AFTER).toJSONString());
        if (eventType.equals(OperatorTypeEnum.DELETE)) {
            dbDataInfo.setData(getJsonObject(struct, BEFORE).toJSONString());
        } else {
            dbDataInfo.setData(getJsonObject(struct, AFTER).toJSONString());
        }
        dbDataInfo.setOperatorType(eventType);
        dbDataInfo.setFileName(Optional.ofNullable(source.get(BIN_FILE)).map(Object::toString).orElse(""));
        dbDataInfo.setFilePos(Optional.ofNullable(source.get(POS)).map(x -> Integer.parseInt(x.toString())).orElse(0));
        dbDataInfo.setDatabase(database);
        dbDataInfo.setTableName(tableName);
        dbDataInfo.setOperatorTime(Optional.ofNullable(struct.get(TS_MS)).map(x -> Long.parseLong(x.toString())).orElseGet(System::currentTimeMillis));
        // 输出数据
        System.out.println("'xxxxxxxxxx'");
        collector.collect(dbDataInfo);
    }

    /**
     *
     */
    private JSONObject getJsonObject(Struct value, String fieldElement) {
        Struct element = value.getStruct(fieldElement);
        JSONObject jsonObject = new JSONObject();
        if (element != null) {
            Schema afterSchema = element.schema();
            List<Field> fieldList = afterSchema.fields();
            for (Field field : fieldList) {
                Object afterValue = element.get(field);
                jsonObject.put(field.name(), afterValue);
            }
        }
        return jsonObject;
    }

    @Override
    public TypeInformation<DbDataInfo> getProducedType() {
        return TypeInformation.of(DbDataInfo.class);
    }
}
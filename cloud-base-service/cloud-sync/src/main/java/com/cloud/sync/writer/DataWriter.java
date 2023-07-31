package com.cloud.sync.writer;

import com.cloud.sync.vo.*;
import org.apache.commons.lang3.tuple.Triple;

import java.sql.Connection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataWriter {

    private static Map<String, CommonWriter> map = new HashMap<>();


    public static synchronized void init(ServeVo serveVo
            , List<TableConfigVo> readerTables, List<TableConfigVo> writerTables, ConnectConfigVo writerConnect) {
        Map<Long, TableConfigVo> readerTableMap = readerTables.stream().collect(Collectors.toMap(TableConfigVo::getId, a -> a, (k1, k2) -> k1));
        Map<Long, TableConfigVo> writerTableMap = writerTables.stream().collect(Collectors.toMap(TableConfigVo::getId, a -> a, (k1, k2) -> k1));
        for (TableAssociateVo tableAssociateVo : serveVo.getTableAssociateVoList()) {
            TableConfigVo readerTable = readerTableMap.get(tableAssociateVo.getReadTableId());
            TableConfigVo writerTable = writerTableMap.get(tableAssociateVo.getWriteTableId());
            List<String> columns = writerTable.getColumnConfigVoList().stream().map(ColumnConfigVo::getColumnName).collect(Collectors.toList());
            Connection connection = DBUtil.getConnect(writerConnect.getId(),
                    DataBaseType.getDataBaseType(writerConnect.getType()).getJdbcUrl(writerConnect.getHostname(), writerConnect.getPort(), writerConnect.getDatabaseName())
                    , writerConnect.getUser(), writerConnect.getPassword(), DataBaseType.getDataBaseType(writerConnect.getType()).getDriverClassName());
            Triple<List<String>, List<Integer>, List<String>> resultSetMetaData = DBUtil.getColumnMetaData(connection, writerTable.getTableName(), columns);
            Map<String, String> associateColumn = new HashMap<>();
            List<ColumnConfigVo> reader = readerTable.getColumnConfigVoList().stream().sorted(Comparator.comparing(ColumnConfigVo::getSeq)).collect(Collectors.toList());
            List<ColumnConfigVo> writer = writerTable.getColumnConfigVoList().stream().sorted(Comparator.comparing(ColumnConfigVo::getSeq)).collect(Collectors.toList());
            for (int i = 0; i < writer.size(); i++) {
                associateColumn.put(writer.get(i).getColumnName().toLowerCase(), reader.get(i).getColumnName().toLowerCase());
            }
            CommonWriter commonWriter = CommonWriter.getCommonWriter(writerConnect.getId(), writerTable.getTableName(), writer
                    , resultSetMetaData, DataBaseType.getDataBaseType(writerConnect.getType()), associateColumn);
            map.put(serveVo.getId() + "_" + readerTable.getTableName().toLowerCase(), commonWriter);
            DBUtil.closeDBResources(null, null, connection);
        }
    }

    public static void writer(Long serveId, String readTable, List<Map<String, String>> buffer) {
        //读取配置信息
        try {
            CommonWriter commonWriter = map.get(serveId + "_" + readTable.toLowerCase());
            commonWriter.doBatchInsert(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

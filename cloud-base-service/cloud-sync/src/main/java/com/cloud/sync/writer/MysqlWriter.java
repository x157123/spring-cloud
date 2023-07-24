package com.cloud.sync.writer;

import java.util.List;

public class MysqlWriter extends CommonWriter {

    private MysqlWriter(){}

    public static MysqlWriter getMysqlWriter(Long dbId, String url, String username, String password, String table, List<String> columns) {
        MysqlWriter mysqlWriter = new MysqlWriter();
        mysqlWriter.init(dbId, DataBaseType.MySql, url, username, password, table, columns);
        return mysqlWriter;
    }

    public void writer(List<Record> buffer) {
        //读取配置信息
        try {
            super.doBatchInsert(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

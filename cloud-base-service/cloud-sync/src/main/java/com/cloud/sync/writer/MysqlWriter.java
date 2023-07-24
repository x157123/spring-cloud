package com.cloud.sync.writer;

import java.util.List;

public class MysqlWriter extends CommonWriter {

    public MysqlWriter(Long dbId, String url, String username, String password, String table, List<String> columns) {
        super.init(dbId, DataBaseType.MySql, url, username, password, table, columns);
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

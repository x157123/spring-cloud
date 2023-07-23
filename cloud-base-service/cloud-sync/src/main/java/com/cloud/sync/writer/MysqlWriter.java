package com.cloud.sync.writer;

import java.util.List;

public class MysqlWriter extends CommonWriter {

    public void init(Long connectionId, DataBaseType dataBaseType, String url, String username, String password, String table, List<String> columns) {
        super.init(connectionId, DataBaseType.MySql, url, username, password, table, columns);
    }


    public void writer(Long serveTableId, List<Record> buffer) {
        //读取配置信息


    }

}

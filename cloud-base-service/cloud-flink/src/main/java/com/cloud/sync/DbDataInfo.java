package com.cloud.sync;

import lombok.Data;

/**
 *
 **/
@Data
public class DbDataInfo {
    /**
     * 变更前数据
     */
    private String beforeData;
    /**
     * 变更后数据
     */
    private String afterData;

    /**
     * 操作的数据
     */
    private String data;

    /**
     * 变更类型
     */
    private OperatorTypeEnum operatorType;
    /**
     * binlog文件名
     */
    private String fileName;
    /**
     * binlog当前读取点位
     */
    private Integer filePos;
    /**
     * 数据库名
     */
    private String database;
    /**
     * 表名
     */
    private String tableName;

    /**
     * 变更时间
     */
    private Long operatorTime;

}
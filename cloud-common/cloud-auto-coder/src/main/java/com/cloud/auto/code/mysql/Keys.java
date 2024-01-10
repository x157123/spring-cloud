package com.cloud.auto.code.mysql;

import lombok.Data;

@Data
public class Keys {

    /**
     * // 外键所在表
     */
    private String fkTableName;
    /**
     * 外键列名
     */
    private String fkColumnName;
    /**
     * 关联表
     */
    private String pkTableName;
    /**
     * 关联表列名
     */
    private String pkColumnName;


    public Keys(String fkTableName,String fkColumnName,String pkTableName,String pkColumnName){
        this.fkTableName = fkTableName;
        this.fkColumnName = fkColumnName;
        this.pkTableName = pkTableName;
        this.pkColumnName = pkColumnName;
    }

}

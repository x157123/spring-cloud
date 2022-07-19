package com.test.read.bo;

import lombok.Data;

/**
 * @author liulei
 */
@Data
public class Database {

    /**
     * ip地址
     */
    private String ip = "";

    /**
     * 端口
     */
    private String port = "";

    /**
     * db
     */
    private String db = "";

    /**
     * sid
     */
    private String sid = "";

    /**
     * 用户
     */
    private String name = "";

    /**
     * 密码
     */
    private String pwd = "";

    /**
     * 数据库类型
     */
    private String dbType;


    public Database(String dbType,String ip, String port, String db, String sid, String name, String pwd) {
        this.dbType = dbType;
        this.ip = ip;
        this.port = port;
        this.db = db;
        this.sid = sid;
        this.name = name;
        this.pwd = pwd;
    }
}

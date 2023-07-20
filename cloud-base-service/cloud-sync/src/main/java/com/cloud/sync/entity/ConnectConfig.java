package com.cloud.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;

/**
 * @author liulei
 * 数据库配置
 */
@Data
@TableName("sync_connect_config")
public class ConnectConfig extends BaseEntity {

    /**
     * 数据库类型
     */
    private Long type;

    /**
     * 连接地址
     */
    private String hostname;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 数据库
     */
    private String databaseName;

    /**
     * 用户
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本
     */
    private Integer version;
}
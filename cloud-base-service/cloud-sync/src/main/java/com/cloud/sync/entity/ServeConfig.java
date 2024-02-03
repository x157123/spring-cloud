package com.cloud.sync.entity;

import com.cloud.common.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author liulei
* 同步启动服务
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sync_serve_config")
public class ServeConfig extends BaseEntity {

    /**
    * 服务名称
    */
    private Long serveId;

    /**
    * 状态0未启动，1停用中，5待启动，10启动
    */
    private Integer state;

    /**
    * 数据库采集偏移情况
    */
    private String offSet;
}

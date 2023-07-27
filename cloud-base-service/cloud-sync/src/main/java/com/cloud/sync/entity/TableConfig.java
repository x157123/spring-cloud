package com.cloud.sync.entity;

import com.cloud.common.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
    import java.util.List;

/**
* @author liulei
* 同步表配置
*/
@Data
@TableName("sync_table_config")
public class TableConfig extends BaseEntity {

    /**
    * 服务id
    */
    private Long serveId;

    /**
    * 1读，2写
    */
    private Integer type;

    /**
    * 数据库表
    */
    private String tableName;

    /**
    * 备注
    */
    private String remark;
}

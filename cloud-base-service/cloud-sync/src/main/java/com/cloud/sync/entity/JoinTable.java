package com.cloud.sync.entity;

import com.cloud.common.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author liulei
* 
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sync_join_table")
public class JoinTable extends BaseEntity {

    /**
    * 连接ID
    */
    private Long connectId;

    /**
    * 名称
    */
    private String name;

    /**
    * 关联表
    */
    private String joinTable;
}

package com.cloud.sync.entity;

import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;
import java.util.List;

/**
 * @author liulei
 * 
 */
@Data
public class TableConfig extends BaseEntity {

	/**
     * 数据库id
     */
    private Long connectId;

	/**
     * 数据库表
     */
    private String tableName;

	/**
     * 备注
     */
    private String remark;
}

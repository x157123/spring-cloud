package com.cloud.sync.entity;

import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;

/**
 * @author liulei
 * 同步数据库列配置
 */
@Data
public class ColumnConfig extends BaseEntity {

	/**
     * 数据库Id
     */
    private Long connectId;

	/**
     * 表Id
     */
    private Long tableId;

	/**
     * 表明
     */
    private String columnName;

	/**
     * 备注
     */
    private String columnRemark;

	/**
     * 数据类型
     */
    private String columnType;

	/**
     * 长度
     */
    private Integer columnLength;

	/**
     * 是否必填
     */
    private Integer columnRequired;

	/**
     * 主键
     */
    private Integer columnPrimaryKey;
}

package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 同步数据库列配置
 */
@Data
public class ColumnConfigQuery {

	/**
     * 
     */
    @Schema(description = "同步数据库列配置")
    private Long id;

	/**
     * 表Id
     */
    @Schema(description = "同步数据库列配置表Id")
    private Long tableId;

	/**
     * 顺序对应
     */
    @Schema(description = "同步数据库列配置顺序对应")
    private Integer seq;

	/**
     * 表明
     */
    @Schema(description = "同步数据库列配置表明")
    private String columnName;

	/**
     * 备注
     */
    @Schema(description = "同步数据库列配置备注")
    private String columnRemark;

	/**
     * 数据类型
     */
    @Schema(description = "同步数据库列配置数据类型")
    private String columnType;

	/**
     * 主键
     */
    @Schema(description = "同步数据库列配置主键")
    private Integer columnPrimaryKey;

	/**
     * 默认值
     */
    @Schema(description = "同步数据库列配置默认值")
    private String def;
}

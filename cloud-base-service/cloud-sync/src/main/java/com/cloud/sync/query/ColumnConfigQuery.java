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
     * 数据库Id
     */
    @Schema(description = "同步数据库列配置数据库Id")
    private Long connectId;

	/**
     * 表Id
     */
    @Schema(description = "同步数据库列配置表Id")
    private Long tableId;

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
     * 长度
     */
    @Schema(description = "同步数据库列配置长度")
    private Integer columnLength;

	/**
     * 是否必填
     */
    @Schema(description = "同步数据库列配置是否必填")
    private Integer columnRequired;

	/**
     * 主键
     */
    @Schema(description = "同步数据库列配置主键")
    private Integer columnPrimaryKey;
}

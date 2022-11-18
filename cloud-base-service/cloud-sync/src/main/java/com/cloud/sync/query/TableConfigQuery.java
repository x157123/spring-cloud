package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 同步表配置
 */
@Data
public class TableConfigQuery {

	/**
     * id
     */
    @Schema(description = "同步表配置id")
    private Long id;

	/**
     * 数据库id
     */
    @Schema(description = "同步表配置数据库id")
    private Long connectId;

	/**
     * 数据库表
     */
    @Schema(description = "同步表配置数据库表")
    private String tableName;

	/**
     * 备注
     */
    @Schema(description = "同步表配置备注")
    private String remark;
}

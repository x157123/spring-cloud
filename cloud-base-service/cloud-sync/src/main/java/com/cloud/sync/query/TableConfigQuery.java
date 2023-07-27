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
     * 服务id
     */
    @Schema(description = "同步表配置服务id")
    private Long serveId;

	/**
     * 1读，2写
     */
    @Schema(description = "同步表配置1读，2写")
    private Integer type;

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

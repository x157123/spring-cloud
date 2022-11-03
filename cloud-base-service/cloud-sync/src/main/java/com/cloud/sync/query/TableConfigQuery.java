package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 
 */
@Data
public class TableConfigQuery {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 数据库id
     */
    @Schema(description = "数据库id")
    private Long connectId;

	/**
     * 数据库表
     */
    @Schema(description = "数据库表")
    private String tableName;

	/**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}

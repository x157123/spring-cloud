package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 
 */
@Data
public class JoinTableQuery {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 连接ID
     */
    @Schema(description = "连接ID")
    private Long connectId;

	/**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

	/**
     * 关联表
     */
    @Schema(description = "关联表")
    private String joinTable;
}

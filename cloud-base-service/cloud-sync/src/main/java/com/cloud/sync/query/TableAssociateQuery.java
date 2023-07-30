package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 
 */
@Data
public class TableAssociateQuery {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 服务Id
     */
    @Schema(description = "服务Id")
    private Long serveId;

	/**
     * 读取表
     */
    @Schema(description = "读取表")
    private Long readTableId;

	/**
     * 写入表
     */
    @Schema(description = "写入表")
    private Long writeTableId;
}

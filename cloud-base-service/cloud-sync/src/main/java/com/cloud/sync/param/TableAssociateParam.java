package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author liulei
 * 
 */
@Data
@Schema(name = "响应对象", description = "响应对象")
public class TableAssociateParam {

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
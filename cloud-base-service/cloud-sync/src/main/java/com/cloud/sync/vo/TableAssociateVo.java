package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;


/**
 * @author liulei
 * 
 */
@Data
@Schema(name = "响应对象", description = "响应对象")
public class TableAssociateVo {
	/**
     * id
     */
    @Schema(description = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
	/**
     * 服务Id
     */
    @Schema(description = "服务Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serveId;
	/**
     * 读取表
     */
    @Schema(description = "读取表")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long readTableId;
	/**
     * 写入表
     */
    @Schema(description = "写入表")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long writeTableId;
}

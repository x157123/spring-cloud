package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;


/**
 * @author liulei
 * 同步启动服务
 */
@Data
@Schema(name = "同步启动服务响应对象", description = "同步启动服务响应对象")
public class ServeConfigVo {
	/**
     * id
     */
    @Schema(description = "同步启动服务id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
	/**
     * 服务名称
     */
    @Schema(description = "同步启动服务服务名称")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serveId;
	/**
     * 状态0未启动，1停用中，5待启动，10启动
     */
    @Schema(description = "同步启动服务状态0未启动，1停用中，5待启动，10启动")
    private Integer state;
	/**
     * 数据库采集偏移情况
     */
    @Schema(description = "同步启动服务数据库采集偏移情况")
    private String offSet;
	/**
     * 版本
     */
    @Schema(description = "同步启动服务版本")
    private Integer version;
}

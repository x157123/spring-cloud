package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author liulei
 * 同步启动服务
 */
@Data
@Schema(name = "同步启动服务响应对象", description = "同步启动服务响应对象")
public class ServeTableVo {
	/**
     * id
     */
    @Schema(description = "同步启动服务id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
	/**
     * 采集任务Id
     */
    @Schema(description = "同步启动服务采集任务Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serveId;
	/**
     * 数据表映射
     */
    @Schema(description = "同步启动服务数据表映射")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tableMapId;
}

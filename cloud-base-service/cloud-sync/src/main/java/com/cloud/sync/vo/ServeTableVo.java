package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;


/**
 * @author liulei
 * 同步表
 */
@Data
@Schema(name = "同步表响应对象", description = "同步表响应对象")
public class ServeTableVo {
	/**
     * id
     */
    @Schema(description = "同步表id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
	/**
     * 采集任务Id
     */
    @Schema(description = "同步表采集任务Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long serveId;
	/**
     * 数据表映射
     */
    @Schema(description = "同步表数据表映射")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tableMapId;
}

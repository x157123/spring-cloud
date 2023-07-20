package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

import java.util.List;
import com.cloud.sync.vo.ServeTableVo;

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
     * 采集数据库Id
     */
    @Schema(description = "同步启动服务采集数据库Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long readConnectId;
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
     * 同步表
     */
    @Schema(description = "同步启动服务同步表")
    private List<ServeTableVo> serveTableVoList;
}

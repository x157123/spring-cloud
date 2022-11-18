package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import com.cloud.sync.vo.ColumnConfigVo;
import com.cloud.sync.vo.TableMapVo;

/**
 * @author liulei
 * 同步表配置
 */
@Data
@Schema(name = "同步表配置响应对象", description = "同步表配置响应对象")
public class TableConfigVo {
	/**
     * id
     */
    @Schema(description = "同步表配置id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
	/**
     * 数据库id
     */
    @Schema(description = "同步表配置数据库id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long connectId;
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

	/**
     * 同步数据库列配置
     */
    @Schema(description = "同步表配置同步数据库列配置")
    private List<ColumnConfigVo> columnConfigVOList;

	/**
     * 表映射
     */
    @Schema(description = "同步表配置表映射")
    private List<TableMapVo> tableMapVOList;
}

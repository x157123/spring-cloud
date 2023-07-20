package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

import java.util.List;
import com.cloud.sync.vo.ServeTableVo;

/**
 * @author liulei
 * 表映射
 */
@Data
@Schema(name = "表映射响应对象", description = "表映射响应对象")
public class TableMapVo {
	/**
     * id
     */
    @Schema(description = "表映射id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
	/**
     * 采集数据Id
     */
    @Schema(description = "表映射采集数据Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long readConnectId;
	/**
     * 读取表Id
     */
    @Schema(description = "表映射读取表Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long readTableId;
	/**
     * 写入数据库Id
     */
    @Schema(description = "表映射写入数据库Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long writeConnectId;
	/**
     * 写入表Id
     */
    @Schema(description = "表映射写入表Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long writeTableId;

	/**
     * 同步表
     */
    @Schema(description = "表映射同步表")
    private List<ServeTableVo> serveTableVoList;
}

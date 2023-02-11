package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 表映射
 */
@Data
@Schema(name = "表映射响应对象", description = "表映射响应对象")
public class TableMapParam {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 采集数据Id
     */
    @Schema(description = "采集数据Id")
    private Long readConnectId;

	/**
     * 读取表Id
     */
    @Schema(description = "读取表Id")
    private Long readTableId;

	/**
     * 写入数据库Id
     */
    @Schema(description = "写入数据库Id")
    private Long writeConnectId;

	/**
     * 写入表Id
     */
    @Schema(description = "写入表Id")
    private Long writeTableId;

	/**
     * 版本
     */
    @Schema(description = "版本")
    private Long version;
}

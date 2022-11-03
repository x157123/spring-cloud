package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotNull;

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
    @Schema(description = "id", example = "1")
    private Long id;

	/**
     * 采集数据Id
     */
    @Schema(description = "采集数据Id", example = "1")
    private Long readConnectId;

	/**
     * 读取表Id
     */
    @Schema(description = "读取表Id", example = "1")
    private Long readTableId;

	/**
     * 写入数据库Id
     */
    @Schema(description = "写入数据库Id", example = "1")
    private Long writeConnectId;

	/**
     * 写入表Id
     */
    @Schema(description = "写入表Id", example = "1")
    private Long writeTableId;

	/**
     * 版本
     */
    @Schema(description = "版本", example = "1")
    private Long version;
}

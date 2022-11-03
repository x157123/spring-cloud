package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 表映射
 */
@Data
public class TableMapQuery {

	/**
     * id
     */
    @Schema(description = "表映射id")
    private Long id;

	/**
     * 采集数据Id
     */
    @Schema(description = "表映射采集数据Id")
    private Long readConnectId;

	/**
     * 读取表Id
     */
    @Schema(description = "表映射读取表Id")
    private Long readTableId;

	/**
     * 写入数据库Id
     */
    @Schema(description = "表映射写入数据库Id")
    private Long writeConnectId;

	/**
     * 写入表Id
     */
    @Schema(description = "表映射写入表Id")
    private Long writeTableId;
}

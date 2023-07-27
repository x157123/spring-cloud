package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 表映射
 */
@Data
public class ServeQuery {

	/**
     * id
     */
    @Schema(description = "表映射id")
    private Long id;

	/**
     * 名称
     */
    @Schema(description = "表映射名称")
    private String name;

	/**
     * 采集数据Id
     */
    @Schema(description = "表映射采集数据Id")
    private Long readConnectId;

	/**
     * 写入数据库Id
     */
    @Schema(description = "表映射写入数据库Id")
    private Long writeConnectId;
}

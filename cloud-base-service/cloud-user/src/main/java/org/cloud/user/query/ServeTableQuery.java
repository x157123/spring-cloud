package org.cloud.user.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 同步表
 */
@Data
public class ServeTableQuery {

	/**
     * id
     */
    @Schema(description = "同步表id")
    private Long id;

	/**
     * 采集任务Id
     */
    @Schema(description = "同步表采集任务Id")
    private Long serveId;

	/**
     * 数据表映射
     */
    @Schema(description = "同步表数据表映射")
    private Long tableMapId;
}

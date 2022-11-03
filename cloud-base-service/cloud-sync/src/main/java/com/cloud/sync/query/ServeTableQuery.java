package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 同步启动服务
 */
@Data
public class ServeTableQuery {

	/**
     * id
     */
    @Schema(description = "同步启动服务id")
    private Long id;

	/**
     * 采集任务Id
     */
    @Schema(description = "同步启动服务采集任务Id")
    private Long serveId;

	/**
     * 数据表映射
     */
    @Schema(description = "同步启动服务数据表映射")
    private Long tableMapId;
}

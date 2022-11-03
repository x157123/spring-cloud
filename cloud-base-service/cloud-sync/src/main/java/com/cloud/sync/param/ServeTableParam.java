package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * @author liulei
 * 同步启动服务
 */
@Data
@Schema(name = "同步启动服务响应对象", description = "同步启动服务响应对象")
public class ServeTableParam {

	/**
     * id
     */
    @Schema(description = "id", example = "1")
    private Long id;

	/**
     * 采集任务Id
     */
    @Schema(description = "采集任务Id", example = "1")
    private Long serveId;

	/**
     * 数据表映射
     */
    @Schema(description = "数据表映射", example = "1")
    private Long tableMapId;

	/**
     * 版本
     */
    @Schema(description = "版本", example = "1")
    private Long version;
}

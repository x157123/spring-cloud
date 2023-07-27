package org.cloud.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 同步表
 */
@Data
@Schema(name = "同步表响应对象", description = "同步表响应对象")
public class ServeTableParam {

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 采集任务Id
     */
    @Schema(description = "采集任务Id")
    private Long serveId;

    /**
     * 数据表映射
     */
    @Schema(description = "数据表映射")
    private Long tableMapId;

    /**
     * 版本
     */
    @Schema(description = "版本")
    private Integer version;
}

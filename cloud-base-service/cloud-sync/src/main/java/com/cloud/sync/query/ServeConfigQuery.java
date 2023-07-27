package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 同步启动服务
 */
@Data
public class ServeConfigQuery {

	/**
     * id
     */
    @Schema(description = "同步启动服务id")
    private Long id;

	/**
     * 服务名称
     */
    @Schema(description = "同步启动服务服务名称")
    private Long serveId;

	/**
     * 状态0未启动，1停用中，5待启动，10启动
     */
    @Schema(description = "同步启动服务状态0未启动，1停用中，5待启动，10启动")
    private Integer state;

	/**
     * 数据库采集偏移情况
     */
    @Schema(description = "同步启动服务数据库采集偏移情况")
    private String offSet;
}

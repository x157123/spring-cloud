package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author liulei
 * 同步启动服务
 */
@Data
@Schema(name = "同步启动服务响应对象", description = "同步启动服务响应对象")
public class ServeConfigParam {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 服务名称
     */
    @Schema(description = "服务名称")
    private Long serveId;

	/**
     * 状态0未启动，1停用中，5待启动，10启动
     */
    @Schema(description = "状态0未启动，1停用中，5待启动，10启动")
    private Integer state;

	/**
     * 数据库采集偏移情况
     */
    @Length(max = 400, message = "同步启动服务数据库采集偏移情况[ServeConfigVo.offSet]长度不能超过400个字符")
    @Schema(description = "数据库采集偏移情况")
    private String offSet;

	/**
     * 版本
     */
    @Schema(description = "版本")
    private Integer version;
}
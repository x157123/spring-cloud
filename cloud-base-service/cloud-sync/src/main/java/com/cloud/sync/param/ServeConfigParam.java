package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

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
    @Schema(description = "id", example = "1")
    private Long id;

	/**
     * 采集数据库Id
     */
    @Schema(description = "采集数据库Id", example = "1")
    private Long readConnectId;

	/**
     * 状态0未启动，1停用中，5待启动，10启动
     */
    @Schema(description = "状态0未启动，1停用中，5待启动，10启动", example = "1")
    private Integer state;

	/**
     * 数据库采集偏移情况
     */
    @Length(max = 200, message = "同步启动服务数据库采集偏移情况[ServeConfigVo.offSet]长度不能超过200个字符")
    @Schema(description = "数据库采集偏移情况")
    private String offSet;

	/**
     * 版本
     */
    @Schema(description = "版本", example = "1")
    private Long version;
}

package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @author liulei
 * 同步表配置
 */
@Data
@Schema(name = "同步表配置响应对象", description = "同步表配置响应对象")
public class TableConfigParam {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 数据库id
     */
    @NotNull(message = "同步表配置数据库id[TableConfigVo.connectId]不能为null")
    @Schema(description = "数据库id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long connectId;

	/**
     * 数据库表
     */
    @Length(max = 100, message = "同步表配置数据库表[TableConfigVo.tableName]长度不能超过100个字符")
    @Schema(description = "数据库表")
    private String tableName;

	/**
     * 备注
     */
    @Length(max = 300, message = "同步表配置备注[TableConfigVo.remark]长度不能超过300个字符")
    @Schema(description = "备注")
    private String remark;

	/**
     * 版本
     */
    @Schema(description = "版本")
    private Long version;
}
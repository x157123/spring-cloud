package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

/**
 * @author liulei
 * 同步数据库列配置
 */
@Data
@Schema(name = "同步数据库列配置响应对象", description = "同步数据库列配置响应对象")
public class ColumnConfigParam {

	/**
     * 
     */
    @Schema(description = "", example = "1")
    private Long id;

	/**
     * 数据库Id
     */
    @Schema(description = "数据库Id", example = "1")
    private Long connectId;

	/**
     * 表Id
     */
    @Schema(description = "表Id", example = "1")
    private Long tableId;

	/**
     * 表明
     */
    @Length(max = 100, message = "同步数据库列配置表明[ColumnConfigVo.columnName]长度不能超过100个字符")
    @Schema(description = "表明")
    private String columnName;

	/**
     * 备注
     */
    @Length(max = 400, message = "同步数据库列配置备注[ColumnConfigVo.columnRemark]长度不能超过400个字符")
    @Schema(description = "备注")
    private String columnRemark;

	/**
     * 数据类型
     */
    @Length(max = 100, message = "同步数据库列配置数据类型[ColumnConfigVo.columnType]长度不能超过100个字符")
    @Schema(description = "数据类型")
    private String columnType;

	/**
     * 长度
     */
    @Schema(description = "长度", example = "1")
    private Integer columnLength;

	/**
     * 是否必填
     */
    @Schema(description = "是否必填", example = "1")
    private Integer columnRequired;

	/**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    private Integer columnPrimaryKey;

	/**
     * 版本
     */
    @Schema(description = "版本", example = "1")
    private Long version;
}

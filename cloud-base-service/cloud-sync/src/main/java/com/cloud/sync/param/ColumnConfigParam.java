package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author liulei
 * 同步数据库列配置
 */
@Data
@Schema(name = "同步数据库列配置响应对象", description = "同步数据库列配置响应对象")
public class ColumnConfigParam {

    public ColumnConfigParam(Long tableId, Integer seq, String columnName, Integer columnPrimaryKey) {
        this.tableId = tableId;
        this.seq = seq;
        this.columnName = columnName;
        this.columnPrimaryKey = columnPrimaryKey;
    }

    /**
     *
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 表Id
     */
    @Schema(description = "表Id")
    private Long tableId;

    /**
     * 顺序对应
     */
    @Schema(description = "顺序对应")
    private Integer seq;

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
     * 主键
     */
    @Schema(description = "主键")
    private Integer columnPrimaryKey;

    /**
     * 默认值
     */
    @Length(max = 100, message = "同步数据库列配置默认值[ColumnConfigVo.def]长度不能超过100个字符")
    @Schema(description = "默认值")
    private String def;

    /**
     * 版本
     */
    @Schema(description = "版本")
    private Integer version;
}
package com.cloud.sync.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author liulei
 * 同步表配置
 */
@Data
@Schema(name = "同步表配置响应对象", description = "同步表配置响应对象")
public class TableConfigParam {

    public TableConfigParam(Long serveId, Integer type, String tableName) {
        this.serveId = serveId;
        this.type = type;
        this.tableName = tableName;
        this.id = IdWorker.getId(this);
    }

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 服务id
     */
    @Schema(description = "服务id")
    private Long serveId;

    /**
     * 1读，2写
     */
    @Schema(description = "1读，2写")
    private Integer type;

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
    private Integer version;
}
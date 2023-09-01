package com.cloud.sync.vo;

import com.cloud.sync.param.AssociateTableParam;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author liulei
 * 表映射
 */
@Data
@Schema(name = "表映射响应对象", description = "表映射响应对象")
public class ServeVo {
    /**
     * id
     */
    @Schema(description = "表映射id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 名称
     */
    @Schema(description = "表映射名称")
    private String name;
    /**
     * 采集数据Id
     */
    @Schema(description = "表映射采集数据Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long readConnectId;

    @Schema(description = "表映射采集数据名称")
    private String readConnectName;

    @Schema(description = "表映射采集数据类型")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long readConnectType;
    /**
     * 写入数据库Id
     */
    @Schema(description = "表映射写入数据库Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long writeConnectId;

    @Schema(description = "表映射写入数据库名称")
    private String writeConnectName;

    @Schema(description = "表映射写入数据库类型")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long writeConnectType;
    /**
     * 版本
     */
    @Schema(description = "表映射版本")
    private Integer version;

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

    /**
     * 表映射
     */
    @Schema(description = "表映射")
    private List<TableAssociateVo> tableAssociateVoList;

    /**
     * 同步表配置
     */
    @Schema(description = "表映射同步表配置")
    private List<TableConfigVo> tableConfigVoList;


    @Schema(description = "表关联")
    private List<AssociateTableParam> tableConfig;
}

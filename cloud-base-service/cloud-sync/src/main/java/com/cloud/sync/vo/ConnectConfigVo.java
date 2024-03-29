package com.cloud.sync.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author liulei
 * 数据库配置
 */
@Data
@Schema(name = "数据库配置响应对象", description = "数据库配置响应对象")
public class ConnectConfigVo {
    /**
     * id
     */
    @Schema(description = "数据库配置id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 数据库类型
     */
    @Schema(description = "数据库配置数据库类型")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long type;
    /**
     * 数据库类型
     */
    @Schema(description = "数据库配置数据库类型")
    private String typeStr;
    /**
     * 连接地址
     */
    @Schema(description = "数据库配置连接地址")
    private String hostname;
    /**
     * 端口
     */
    @Schema(description = "数据库配置端口")
    private Integer port;
    /**
     * 数据库
     */
    @Schema(description = "数据库配置数据库")
    private String databaseName;
    /**
     * 用户
     */
    @Schema(description = "数据库配置用户")
    private String user;
    /**
     * 密码
     */
    @Schema(description = "数据库配置密码")
    private String password;
    /**
     * 表前缀
     */
    @Schema(description = "数据库配置表前缀")
    private String tablePrefix;
    /**
     * 备注
     */
    @Schema(description = "数据库配置备注")
    private String remark;
    /**
     * 版本
     */
    @Schema(description = "数据库配置版本")
    private Integer version;

    /**
     *
     */
    @Schema(description = "数据库配置")
    private List<JoinTableVo> joinTableVoList;

    /**
     * 表映射
     */
    @Schema(description = "数据库配置表映射")
    private List<ServeVo> serveVoList;
}

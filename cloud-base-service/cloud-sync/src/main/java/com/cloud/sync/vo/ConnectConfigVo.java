package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import com.cloud.sync.vo.columnConfigVo;
import com.cloud.sync.vo.serveConfigVo;
import com.cloud.sync.vo.tableConfigVo;
import com.cloud.sync.vo.tableMapVo;

/**
 * @author liulei
 * 数据库配置
 */
@Data
@Schema(name = "数据库配置响应对象", description = "数据库配置响应对象")
public class connectConfigVo {
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
    private String database;
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
    @JsonSerialize(using = ToStringSerializer.class)
    private Long version;

	/**
     * 同步数据库列配置
     */
    @Schema(description = "数据库配置同步数据库列配置")
    private List<columnConfigVo> columnConfigVOList;

	/**
     * 同步启动服务
     */
    @Schema(description = "数据库配置同步启动服务")
    private List<serveConfigVo> serveConfigVOList;

	/**
     * 同步表配置
     */
    @Schema(description = "数据库配置同步表配置")
    private List<tableConfigVo> tableConfigVOList;

	/**
     * 表映射
     */
    @Schema(description = "数据库配置表映射")
    private List<tableMapVo> tableMapVOList;
}

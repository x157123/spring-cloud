package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author liulei
 * 数据库配置
 */
@Data
@Schema(name = "数据库配置响应对象", description = "数据库配置响应对象")
public class ConnectConfigParam {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 数据库类型
     */
    @NotNull(message = "数据库配置数据库类型[ConnectConfigVo.type]不能为null")
    @Schema(description = "数据库类型")
    private Long type;

	/**
     * 连接地址
     */
    @Length(max = 200, message = "数据库配置连接地址[ConnectConfigVo.hostname]长度不能超过200个字符")
    @Schema(description = "连接地址")
    private String hostname;

	/**
     * 端口
     */
    @Schema(description = "端口")
    private Integer port;

	/**
     * 数据库
     */
    @Length(max = 100, message = "数据库配置数据库[ConnectConfigVo.database]长度不能超过100个字符")
    @Schema(description = "数据库")
    private String database;

	/**
     * 用户
     */
    @Length(max = 100, message = "数据库配置用户[ConnectConfigVo.user]长度不能超过100个字符")
    @Schema(description = "用户")
    private String user;

	/**
     * 密码
     */
    @Length(max = 100, message = "数据库配置密码[ConnectConfigVo.password]长度不能超过100个字符")
    @Schema(description = "密码")
    private String password;

	/**
     * 表前缀
     */
    @Length(max = 100, message = "数据库配置表前缀[ConnectConfigVo.tablePrefix]长度不能超过100个字符")
    @Schema(description = "表前缀")
    private String tablePrefix;

	/**
     * 备注
     */
    @Length(max = 300, message = "数据库配置备注[ConnectConfigVo.remark]长度不能超过300个字符")
    @Schema(description = "备注")
    private String remark;

	/**
     * 版本
     */
    @Schema(description = "版本")
    private Long version;
}

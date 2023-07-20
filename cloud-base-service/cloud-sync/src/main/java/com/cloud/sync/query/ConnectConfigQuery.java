package com.cloud.sync.query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liulei
 * 数据库配置
 */
@Data
public class ConnectConfigQuery {

	/**
     * id
     */
    @Schema(description = "数据库配置id")
    private Long id;

	/**
     * 数据库类型
     */
    @Schema(description = "数据库配置数据库类型")
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
}

package com.cloud.sync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;


/**
 * @author liulei
 * 
 */
@Data
@Schema(name = "响应对象", description = "响应对象")
public class JoinTableVo {
	/**
     * id
     */
    @Schema(description = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
	/**
     * 连接ID
     */
    @Schema(description = "连接ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long connectId;
	/**
     * 名称
     */
    @Schema(description = "名称")
    private String name;
	/**
     * 关联表
     */
    @Schema(description = "关联表")
    private String joinTable;
	/**
     * 版本
     */
    @Schema(description = "版本")
    private Integer version;
}

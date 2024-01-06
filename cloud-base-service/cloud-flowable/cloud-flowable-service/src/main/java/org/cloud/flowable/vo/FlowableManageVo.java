package org.cloud.flowable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * @author liulei
 * 流程管理
 */
@Data
@Schema(name = "流程管理响应对象", description = "流程管理响应对象")
public class FlowableManageVo implements Serializable {
	/**
     * id
     */
    @Schema(description = "流程管理id")
    private String id;
	/**
     * 流程名称
     */
    @Schema(description = "流程管理流程名称")
    private String processName;
	/**
     * 任务key
     */
    @Schema(description = "流程管理任务key")
    private String key;
	/**
     * 时间
     */
    @Schema(description = "流程管理时间")
    private java.util.Date createTime;
}

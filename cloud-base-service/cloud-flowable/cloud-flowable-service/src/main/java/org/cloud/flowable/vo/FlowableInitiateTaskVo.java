package org.cloud.flowable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * @author liulei
 * 发起任务
 */
@Data
@Schema(name = "发起任务响应对象", description = "发起任务响应对象")
public class FlowableInitiateTaskVo implements Serializable {
	/**
     * 
     */
    @Schema(description = "发起任务")
    private String id;
	/**
     * 任务名称
     */
    @Schema(description = "发起任务任务名称")
    private String taskName;
	/**
     * 名称
     */
    @Schema(description = "发起任务名称")
    private String processName;
	/**
     * 状态
     */
    @Schema(description = "发起任务状态")
    private String status;
	/**
     * 开始时间
     */
    @Schema(description = "发起任务开始时间")
    private java.util.Date startTime;
	/**
     * 流程id
     */
    @Schema(description = "发起任务流程id")
    private String processInstanceId;
}

package org.cloud.flowable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * @author liulei
 * 代办任务
 */
@Data
@Schema(name = "代办任务响应对象", description = "代办任务响应对象")
public class FlowableTaskVo implements Serializable {
	/**
     * id
     */
    @Schema(description = "代办任务id")
    private String id;
	/**
     * 任务名称
     */
    @Schema(description = "代办任务任务名称")
    private String taskName;
	/**
     * 名称
     */
    @Schema(description = "代办任务名称")
    private String processName;
	/**
     * 状态
     */
    @Schema(description = "代办任务状态")
    private String status;
	/**
     * 开始时间
     */
    @Schema(description = "代办任务开始时间")
    private java.util.Date startTime;
	/**
     * 流程id
     */
    @Schema(description = "代办任务流程id")
    private String processInstanceId;
}

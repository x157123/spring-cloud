package org.cloud.flowable.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FlowableProcessingStatus implements Serializable {

    /**
     * 表达式
     */
    private String conditionExpression;

    /**
     * 补充参数名称
     */
    private String expressionName;

    /**
     * 补充参数需要的值
     */
    private String expressionValue;

    /**
     * 任务名称
     */
    private String nextTaskName;

    /**
     * 办理用户
     */
    private List<String> assignees;

    /**
     * 办理组
     */
    private List<String> groups;

    /**
     * 候选用户
     */
    private List<String> candidateUsers;

    /**
     *  候选组
     */
    private List<String> candidateGroups;
}

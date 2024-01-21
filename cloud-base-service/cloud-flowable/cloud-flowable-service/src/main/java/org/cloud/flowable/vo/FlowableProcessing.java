package org.cloud.flowable.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class FlowableProcessing implements Serializable {

    /**
     * 类型  0 线条   1 接单
     */
    private int type;

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
     * 候选组
     */
    private List<String> candidateGroups;

    /**
     * 下级节点
     */
    private List<FlowableProcessing> flowableProcessing;

    /**
     * 创建线条
     *
     * @return
     */
    public static FlowableProcessing createLine(String conditionExpression) {
        FlowableProcessing node = new FlowableProcessing();
        node.setType(0);
        node.setConditionExpression(conditionExpression);
        return node;
    }


    /**
     * 创建节点
     *
     * @return
     */
    public static FlowableProcessing createNode(String nextTaskName, String assignees, List<String> candidateUsers, List<String> candidateGroups) {
        FlowableProcessing node = new FlowableProcessing();
        node.setType(1);
        node.nextTaskName = nextTaskName;
        node.assignees = assignees == null || assignees.isEmpty() ? null : Arrays.asList(assignees.split(","));
        node.candidateUsers = candidateUsers;
        node.candidateGroups = candidateGroups;
        return node;
    }

    public void add(FlowableProcessing data) {
        if (flowableProcessing == null) {
            flowableProcessing = new ArrayList<>();
        }
        flowableProcessing.add(data);
    }

}

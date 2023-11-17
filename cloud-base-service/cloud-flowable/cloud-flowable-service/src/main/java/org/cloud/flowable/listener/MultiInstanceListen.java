package org.cloud.flowable.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class MultiInstanceListen implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        FlowElement element = execution.getCurrentFlowElement();
        if (element instanceof UserTask) {
            UserTask userTask = (UserTask) element;
            List<String> candidateGroups = userTask.getCandidateGroups();

            // 多任务时，每个任务都会执行一次这个监听器，所以更新、插入操作需要小心，避免重复操作
            Object flag = execution.getVariable(userTask.getId().concat("assigneeList"));


            Set<String> set = new HashSet<>();
            set.addAll(candidateGroups);

            set.add("user1");
            set.add("user2");
            set.add("user3");
            set.add("user4");



            if (flag==null) {
                log.info("candidateGroups value: {}", candidateGroups.toString());
                // 设置 setVariableLocal 会导致找不到 assigneeList 变量
                // userTask.getId() 就是节点定义ID，拼上它，可以解决一个流程里面多个审批节点问题
                execution.setVariable("assigneeList", set);
            }
            flag = execution.getVariable("assigneeList");
            System.out.println(flag);
        }
    }
}

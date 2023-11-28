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
            Set<String> set = new HashSet<>();
            set.addAll(candidateGroups);

            Object assignee = execution.getVariable("assignee");
            Object assignees = execution.getVariable("assignees");

            set.add("user1");
            set.add("user2");
            set.add("user3");
            set.add("user4");
            execution.setVariable("assigneeList", set);
            Object flag = execution.getVariable("assigneeList");
            log.info("candidateGroups value: {}", flag);
        }
    }
}

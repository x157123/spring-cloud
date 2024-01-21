package org.cloud.flowable.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class MultiTaskListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {

        Set<String> set = new HashSet<>();
        set.add("user44");
        set.add("user55");
        set.add("user66");
        set.add("user77");

        log.info("candidateGroups value: {}", set.toString());
        // 设置 setVariableLocal 会导致找不到 assigneeList 变量
        // userTask.getId() 就是节点定义ID，拼上它，可以解决一个流程里面多个审批节点问题
        delegateTask.setVariable("assigneeList", set);
        Object flag = delegateTask.getVariable("assigneeList");
        System.out.println(flag);
    }
}
package org.cloud.flowable.listener;

import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component
public class ApplyTaskListener implements TaskListener {

    private TaskService taskService;

    public ApplyTaskListener(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("自动设置用户:-->" + delegateTask.getVariable("assignee"));
        if (delegateTask.getVariable("assignee") != null) {
            String assignee = (String) delegateTask.getVariable("assignee");
            taskService.setAssignee(delegateTask.getId(), assignee);
        }
    }
}
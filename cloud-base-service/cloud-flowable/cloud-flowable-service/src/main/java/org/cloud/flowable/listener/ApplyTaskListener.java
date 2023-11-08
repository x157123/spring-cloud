package org.cloud.flowable.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class ApplyTaskListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("自动设置用户:-->" + delegateTask.getVariable("assignee"));
        Integer lon = delegateTask.getVariable("day", Integer.class);
        if (delegateTask.getVariable("assignee") != null) {
            String assignee = (String) delegateTask.getVariable("assignee");
        }
        delegateTask.setAssignee("55555");
    }
}
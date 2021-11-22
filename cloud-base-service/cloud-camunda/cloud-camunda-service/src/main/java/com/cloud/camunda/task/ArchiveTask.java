package com.cloud.camunda.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * @author liulei
 */
public class ArchiveTask  implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("审核流程 - SERVICE TASK - 回调");
    }
}


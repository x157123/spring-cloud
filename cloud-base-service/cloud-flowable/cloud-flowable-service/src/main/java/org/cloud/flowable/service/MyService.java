package org.cloud.flowable.service;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyService {

    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Transactional
    public void createDeployment(String resourceName, String bpmnXmlStr) {
        repositoryService.createDeployment()
                .addString(resourceName, bpmnXmlStr)
                .deploy();
    }

    @Transactional
    public List<Deployment> getDeployment() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        return list;
    }


    public void deleteDeployment(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        repositoryService.deleteDeployment(deployId, true);
    }

    @Transactional
    public void startProcess(String processInstanceById) {
        runtimeService.startProcessInstanceById(processInstanceById);
    }

    @Transactional
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

}
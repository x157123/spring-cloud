package com.cloud.camunda.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liulei
 */
@RestController
@Api(value = "任务办理", tags = "任务办理")
public class TaskController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 启动流程
     *
     * @param key
     * @return
     */
    @GetMapping("/startProcess/{key}")
    @ApiOperation(value = "启动流程", notes = "启动流程")
    public String startProcess(@PathVariable String key) {
        Execution execution = runtimeService.startProcessInstanceByKey(key);
        //流程实例ID
        System.out.println("流程实例ID：" + execution.getId());
        //流程定义ID
        System.out.println("流程定义ID：" + execution.getProcessInstanceId());
        return execution.getId();
    }


    /**
     * 获取代办
     *
     * @param assignee
     * @param first
     * @param max
     * @return
     */
    @GetMapping("/getTasksByAssignee/{assignee}/{first}/{max}")
    @ApiOperation(value = "获取代办列表", notes = "获取代办列表")
    @ResponseBody
    public Map<String, String> getTasksByAssignee(@PathVariable String assignee, @PathVariable Integer first, @PathVariable Integer max) {
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().asc().listPage(first, max);
        Map<String, String> map = new HashMap<>();
        list.forEach(task -> {
            map.put(task.getId(), task.getName());
        });
        return map;
    }

    /**
     * 设置节点办理人
     *
     * @param taskId
     * @param assignee
     */
    @GetMapping("/assignTask/{taskId}/{assignee}")
    @ApiOperation(value = "设置节点办理人", notes = "设置节点办理人")
    public void assignTask(String taskId, String assignee) {
        taskService.setAssignee(taskId, assignee);
    }


    /**
     * 办理
     *
     * @param taskId
     * @param passed
     */
    @GetMapping("/approve")
    @ApiOperation(value = "办理任务", notes = "办理任务")
    public void approveTask(@RequestParam String taskId, @RequestParam boolean passed) {
        Map<String, Object> map = new HashMap(1);
        map.put("passed", passed);
        taskService.complete(taskId, map);
    }


    @GetMapping("/gerProcessDefinition")
    @ApiOperation(value = "获取部署流程", notes = "获取部署流程")
    public Map<String, Object> gerProcessDefinition() {
        Map<String, Object> map = new HashMap(10);
        List<ProcessDefinition> lists = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition deployment : lists) {
            map.put(deployment.getKey(), deployment.getName());
        }
        return map;
    }

    @GetMapping("/deploy")
    @ApiOperation(value = "部署流程", notes = "部署流程")
    public void deploy(String resourceName, String text) {
        Deployment deployment = repositoryService.createDeployment().name("部署名称").addString(resourceName, text).deploy();
    }
}

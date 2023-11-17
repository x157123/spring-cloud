package org.cloud.flowable.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.cloud.flowable.service.MyService;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MyRestController {

    private MyService myService;

    public MyRestController(MyService myService) {
        this.myService = myService;
    }

    /**
     * 创建流程
     *
     * @param bpmnXmlStr
     */
    @PostMapping(value = "/createDeployment")
    public void createDeployment(String bpmnXmlStr) {
        myService.createDeployment(bpmnXmlStr);
    }

    /**
     * 删除数据
     *
     * @param deployId
     */
    @PostMapping(value = "/delete")
    public void delete(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        myService.deleteDeployment(deployId);
    }

    @PostMapping(value = "/single")
    public void single(String deployId) {
        myService.single(deployId);
    }

    @PostMapping(value = "/getDeployment")
    public List<Map<String, Object>> getDeployment() {
        return myService.getDeployment();
    }

    @PostMapping(value = "/testFlow")
    public void testFlow() {
        myService.testFlow();
    }

    /**
     * 开始任务
     *
     * @param day
     * @param assignee
     * @param flowKey
     */
    @GetMapping("startFlow")
    public void startFlow(Integer day, String assignee, String flowKey) {
        myService.startFlow(day, assignee, flowKey);
    }


    /**
     * 指定节点处理人
     *
     * @param assignee
     * @param taskId
     */
    @GetMapping(value = "/setAssignee")
    public void setAssignee(String assignee, String taskId) {
        myService.setAssignee(assignee, taskId);
    }

    /**
     * 获取任务
     *
     * @param assignee
     * @param group
     * @return
     */
    @GetMapping(value = "/getTasks")
    public List<TaskRepresentation> getTasks(String assignee, String group) {
        List<Task> tasks = myService.getTasks(assignee, group);
        List<TaskRepresentation> taskRepresentations = new ArrayList<>();
        for (Task task : tasks) {
            taskRepresentations.add(new TaskRepresentation(task.getId(), task.getName(), task.getAssignee(), task.getProcessInstanceId()));
        }
        return taskRepresentations;
    }


    /**
     * 办理任务
     *
     * @param assignee
     * @param taskId
     * @param outcome
     */
    @GetMapping(value = "/complete")
    public void complete(String assignee, String taskId, String outcome) {
        myService.complete(assignee, taskId, outcome);
    }

    @GetMapping(value = "/getData")
    public void getData() {
        myService.getData();
    }

    @GetMapping(value = "/getHistoricActivityInstance")
    public void getHistoricActivityInstance(String processInstanceId) {
        myService.getHistoricActivityInstance(processInstanceId);
    }


    @GetMapping(value = "/genProcessDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) {
        myService.genProcessDiagram(httpServletResponse, processId);
    }

    @GetMapping(value = "/contextLoads")
    public void contextLoads() {
        myService.contextLoads();
    }

    @Data
    static class TaskRepresentation {
        private String id;
        private String name;
        private String assignee;

        private String processId;

        public TaskRepresentation(String id, String name, String assignee, String processId) {
            this.id = id;
            this.name = name;
            this.assignee = assignee;
            this.processId = processId;
        }
    }
}

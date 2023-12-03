package org.cloud.flowable.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.cloud.flowable.service.MyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping(value = "/deleteDeployment")
    public void deleteDeployment(String deployId) {
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


    /**
     * 开始任务
     *
     * @param day
     * @param assignee
     * @param flowKey
     */
    @GetMapping("startFlow")
    public void startFlow(Integer day, String assignee, String assignees, String flowKey) {
        myService.startFlow(day, assignee, assignees, flowKey);
    }



    /**
     * 暂停流程
     *
     * @param processInstanceId
     */
    @GetMapping("suspend")
    public void suspend(String processInstanceId) {
        myService.suspend(processInstanceId);
    }


    /**
     * 活动任务
     *
     * @param processInstanceId
     */
    @GetMapping("activate")
    public void activate(String processInstanceId) {
        myService.activate(processInstanceId);
    }

    /**
     * 删除流程实例
     *
     * @param processInstanceId
     */
    @GetMapping("delete")
    public void delete(String processInstanceId) {
        myService.delete(processInstanceId);
    }


    /**
     * @param userId
     */
    @GetMapping("getUserStartFlow")
    public List<Map<String, Object>> getUserStartFlow(String userId) {
        return myService.getUserStartFlow(userId);
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
     * 指定节点用户组
     *
     * @param group
     * @param taskId
     */
    @GetMapping(value = "/setGroup")
    public void setGroup(String group, String taskId) {
        myService.setGroup(group, taskId);
    }

    /**
     * 获取任务
     *
     * @param assignee
     * @param group
     * @return
     */
    @GetMapping(value = "/getTasks")
    public List<Map<String, Object>> getTasks(String assignee, String group) {
        List<Map<String, Object>> tasks = myService.getTasks(assignee, group);
        return tasks;
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

    /**
     * 获取流程办理记录
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping(value = "/getHistoricActivityInstance")
    public List<Map<String, Object>> getHistoricActivityInstance(String processInstanceId) {
        return myService.getHistoricActivityInstance(processInstanceId);
    }

    /**
     * 获取下一个节点信息
     *
     * @param currentTaskId
     * @return
     */
    @GetMapping(value = "/getNextFlowElement")
    public List<Map<String, Object>> getNextFlowElement(String currentTaskId) {
        return myService.getNextFlowElement(currentTaskId);
    }


    /**
     * 获取当前办理人
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping(value = "/getActivityInstance")
    public List<Map<String, Object>> getActivityInstance(String processInstanceId) {
        return myService.getActivityInstance(processInstanceId);
    }

    /**
     * 获取流程图
     *
     * @param httpServletResponse
     * @param processId
     */
    @GetMapping(value = "/genProcessDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) {
        myService.genProcessDiagram(httpServletResponse, processId);
    }

    @GetMapping(value = "/contextLoads")
    public void contextLoads() {
        myService.contextLoads();
    }
}

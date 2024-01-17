package org.cloud.flowable.controller;

import com.cloud.common.core.result.ResultBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.cloud.flowable.service.MyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(description = "流程办理", name = "流程办理")
public class MyRestController {

    private final MyService myService;

    public MyRestController(MyService myService) {
        this.myService = myService;
    }

    /**
     * 创建流程
     *
     * @param bpmnXmlStr
     */
    @PostMapping(value = "/createDeployment")
    @Operation(summary = "流程定义-部署流程", description = "部署流程")
    public ResultBody createDeployment(String bpmnXmlStr) {
        return ResultBody.success(myService.createDeployment(bpmnXmlStr));
    }

    /**
     * 删除数据
     *
     * @param deployId
     */
    @PostMapping(value = "/deleteDeployment")
    @Operation(summary = "流程定义-删除流程", description = "删除流程")
    public ResultBody deleteDeployment(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        return ResultBody.success(myService.deleteDeployment(deployId));
    }

    @PostMapping(value = "/getDeployment")
    @Operation(summary = "流程定义-获取部署的流程", description = "获取部署的流程")
    public ResultBody getDeployment() {
        return ResultBody.success(myService.getDeployment());
    }


    /**
     * 开始任务
     *
     * @param day
     * @param assignee
     * @param flowKey
     */
    @PostMapping("startFlow")
    @Operation(summary = "发起任务", description = "发起任务")
    public void startFlow(Integer day, String assignee, String flowKey, String userId) {
        myService.startFlow(day, assignee, flowKey, userId);
    }


    /**
     * 暂停流程
     *
     * @param processInstanceId
     */
    @GetMapping("suspend")
    @Operation(summary = "暂停流程", description = "暂停流程")
    public void suspend(String processInstanceId) {
        myService.suspend(processInstanceId);
    }


    /**
     * 激活任务
     *
     * @param processInstanceId
     */
    @GetMapping("activate")
    @Operation(summary = "激活任务", description = "激活任务")
    public void activate(String processInstanceId) {
        myService.activate(processInstanceId);
    }

    /**
     * 删除任务
     *
     * @param processInstanceId
     */
    @GetMapping("delete")
    @Operation(summary = "删除任务", description = "删除任务")
    public void delete(String processInstanceId) {
        myService.delete(processInstanceId);
    }


    /**
     * @param userId 发起用户
     */
    @PostMapping("getUserStartFlow")
    @Operation(summary = "获取用户流程", description = "获取用户流程")
    public ResultBody getUserStartFlow(String userId) {
        return ResultBody.success(myService.getUserStartFlow(userId));
    }


    /**
     * 指定节点处理人
     *
     * @param assignee
     * @param taskId
     */
    @GetMapping(value = "/setAssignee")
    @Operation(summary = "指定节点处理人", description = "指定节点处理人")
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
    @Operation(summary = "指定节点用户组", description = "指定节点用户组")
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
    @PostMapping(value = "/getTasks")
    @Operation(summary = "获取代办任务", description = "获取代办任务")
    public ResultBody getTasks(String assignee, String group) {
        return ResultBody.success( myService.getTasks(assignee, group));
    }


    /**
     * 办理任务
     *
     * @param assignee
     * @param taskId
     * @param outcome
     */
    @PostMapping(value = "/complete")
    @Operation(summary = "办理任务", description = "办理任务")
    public ResultBody complete(String assignee, String taskId, String outcome) {
        return ResultBody.success(myService.complete(assignee, taskId, outcome));
    }

    @GetMapping(value = "/getData")
    @Operation(summary = "获取所以流程", description = "获取所以流程")
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
    @Operation(summary = "获取流程办理记录", description = "获取流程办理记录")
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
    @Operation(summary = "获取下一个节点信息", description = "获取下一个节点信息")
    public List<Map<String, Object>> getNextFlowElement(String currentTaskId) {
        return myService.getNextFlowElement(currentTaskId);
    }

    /**
     * 获取下一个节点信息
     *
     * @param currentTaskId
     * @return
     */
    @GetMapping(value = "/getNextUser")
    @Operation(summary = "获取下一个节点信息", description = "获取下一个节点信息")
    public List<Map<String, Object>> getNextUser(String currentTaskId) {
        return myService.getNextUser(currentTaskId);
    }


    /**
     * @param taskId
     * @return
     */
    @GetMapping(value = "/getAllFlowElement")
    @Operation(summary = "通过任务Id获取流程详细", description = "通过任务Id获取流程详细")
    public List<Map<String, String>> getAllFlowElement(String taskId, Integer day) {
        Map<String, Object> data = new HashMap<>();
        data.put("day", day);
        data.put("outcome", "通过");
        return myService.getAllFlowElement(taskId, data);
    }


    @GetMapping(value = "/getAllFlowElementByKey")
    @Operation(summary = "通过流程Id获取流程详细", description = "通过流程Id获取流程详细")
    public List<Map<String, String>> getAllFlowElementByKey(String processKey, Integer day) {
        Map<String, Object> data = new HashMap<>();
        data.put("day", day);
        data.put("outcome", "通过");
        return myService.getAllFlowElementByKey(processKey, data);
    }

    /**
     * 获取当前办理人
     *
     * @param processInstanceId
     * @return
     */
    @GetMapping(value = "/getActivityInstance")
    @Operation(summary = "获取当前办理人", description = "获取当前办理人")
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
    @Operation(summary = "获取流程图", description = "获取流程图")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) {
        myService.genProcessDiagram(httpServletResponse, processId);
    }

}

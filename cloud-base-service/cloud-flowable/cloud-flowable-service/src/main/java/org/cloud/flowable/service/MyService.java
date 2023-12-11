package org.cloud.flowable.service;

import jakarta.servlet.http.HttpServletResponse;
import org.cloud.flowable.utils.XmlUtil;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cmd.SetProcessInstanceBusinessStatusCmd;
import org.flowable.engine.impl.cmd.SetProcessInstanceNameCmd;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MyService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    IdentityService identityService;

    @Transactional
    public void createDeployment(String bpmnXmlStr) {
        Map<String, String> map = XmlUtil.getXmlMsg(bpmnXmlStr);
        String resourceName = map.get("name");
        String key = map.get("id");

        Deployment deployment = repositoryService.createDeployment()
                .addString(key + ".bpmn20.xml", bpmnXmlStr)
                .key(key).name(resourceName).deploy();
        System.out.println("部署成功:->" + deployment.getId());
    }


    public List<Map<String, Object>> getDeployment() {
        List<Map<String, Object>> data = new ArrayList<>();
        List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymentTime().desc().list();
        list.stream().forEach(deployment -> {
            System.out.println("id:" + deployment.getId() + "   key:" + deployment.getKey());
            Map<String, Object> map = new HashMap<>();
            map.put("id", deployment.getId());
            map.put("key", deployment.getKey());
            map.put("name", deployment.getName());
            map.put("time", deployment.getDeploymentTime());
            data.add(map);
        });
        return data;
    }

    public void single(String deployId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());
    }

    public void deleteDeployment(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        repositoryService.deleteDeployment(deployId, true);
    }

    /**
     * 获取任务
     *
     * @param assignee
     * @param group
     * @return
     */
    public List<Map<String, Object>> getTasks(String assignee, String group) {
        List<Map<String, Object>> listData = new ArrayList<>();
        TaskQuery taskQuery = taskService.createTaskQuery().or();
        if (assignee != null) {
            taskQuery = taskQuery.taskAssignee(assignee);
        }
        if (group != null) {
            taskQuery = taskQuery.taskCandidateGroup(group);
        }
        List<Task> list = taskQuery.endOr().list();
        if (list.size() > 0) {
            //根据节点ID 获取流程
            Set<String> processInstanceIds = list.stream().map(Task::getProcessInstanceId).collect(Collectors.toSet());
            List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIds).list();
            Map<String, HistoricProcessInstance> processInstanceMap = historicProcessInstances.stream()
                    .collect(Collectors.toMap(HistoricProcessInstance::getId, // Key 是 HistoricProcessInstance 的 ID
                            historicProcessInstance -> historicProcessInstance));
            list.forEach(task -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", task.getId());
                map.put("taskName", task.getName());
                map.put("assignee", task.getAssignee());
                map.put("processInstanceId", task.getProcessInstanceId());
                map.put("task", processInstanceMap.get(task.getProcessInstanceId()).getName());
                listData.add(map);
            });
        }
        return listData;
    }

    /**
     * 办理任务
     *
     * @param assignee
     * @param taskId
     * @param outcome
     */
    public void complete(String assignee, String taskId, String outcome) {
        taskService.setAssignee(taskId, assignee);
        Map<String, Object> principalMap = new HashMap<>();
        principalMap.put("outcome", outcome);
        taskService.complete(taskId, principalMap);
    }

    /**
     * 开始流程
     *
     * @param day
     * @param assignee
     */
    public void startFlow(Integer day, String assignee, String assignees, String processKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("day", day);

        ProcessDefinition processDefinition = getLatestProcessDefinition(processKey);
        Authentication.setAuthenticatedUserId("123");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), map);
        Authentication.setAuthenticatedUserId(null);


        if (assignee != null) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            taskService.setAssignee(task.getId(), assignee);
        }

        // 创建 SetProcessInstanceNameCmd 命令   设置任务名称
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        CommandExecutor commandExecutor = processEngineConfiguration.getCommandExecutor();
        commandExecutor.execute(new SetProcessInstanceBusinessStatusCmd(processInstance.getId(), "开始"));
        commandExecutor.execute(new SetProcessInstanceNameCmd(processInstance.getId(), "小明请假流程"));


        System.out.println(processInstance.getId());
    }


    /**
     * 暂停任务
     *
     * @param processInstanceId
     */
    public void suspend(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        CommandExecutor commandExecutor = processEngineConfiguration.getCommandExecutor();
        commandExecutor.execute(new SetProcessInstanceBusinessStatusCmd(processInstanceId, "暂停"));
    }

    /**
     * 挂载任务
     *
     * @param processInstanceId
     */
    public void activate(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        CommandExecutor commandExecutor = processEngineConfiguration.getCommandExecutor();
        commandExecutor.execute(new SetProcessInstanceBusinessStatusCmd(processInstanceId, "开始"));
    }

    /**
     * 删除任务
     *
     * @param processInstanceId
     */
    public void delete(String processInstanceId) {
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        CommandExecutor commandExecutor = processEngineConfiguration.getCommandExecutor();
        commandExecutor.execute(new SetProcessInstanceBusinessStatusCmd(processInstanceId, "删除"));
        runtimeService.deleteProcessInstance(processInstanceId, "不可用");
    }


    public List<Map<String, Object>> getUserStartFlow(String userId) {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId).orderByProcessInstanceStartTime().desc().list();
        List<Map<String, Object>> listData = new ArrayList<>();
        list.forEach(t -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getId());
            map.put("name", t.getName());
            map.put("processName", t.getProcessDefinitionName());
            map.put("startTime", t.getStartTime());
            map.put("status", t.getBusinessStatus());
            listData.add(map);
        });
        return listData;
    }

    // 获取最新版本的流程定义
    private ProcessDefinition getLatestProcessDefinition(String processKey) {
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult();
        return processDefinition;
    }

    /**
     * 指定节点处理人
     *
     * @param assignee
     * @param taskId
     */
    public void setAssignee(String assignee, String taskId) {
        taskService.setAssignee(taskId, assignee);
    }

    /**
     * 指定节点处理人
     *
     * @param group
     * @param taskId
     */
    public void setGroup(String group, String taskId) {
        taskService.addCandidateGroup(taskId, group);
    }


    public void getData() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance processInstance : list) {
            System.out.println(processInstance.getBusinessStatus());
        }
        System.out.println(list.size());
    }


    public List<Map<String, Object>> getHistoricActivityInstance(String processInstanceId) {

        List<Map<String, Object>> list = new ArrayList<>();
        // 查看历史
        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery().
                processInstanceId(processInstanceId).activityTypes(Arrays.asList("userTask").stream().collect(Collectors.toSet()))
                .finished().orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance activity : activities) {
            Map<String, Object> map = new HashMap<>();
            map.put("activityName", activity.getActivityName());
            map.put("assignee", activity.getAssignee());
            map.put("endTime", activity.getEndTime());
            list.add(map);
        }
        return list;
    }


    /**
     * @param currentTaskId
     * @return
     */
    public List<Map<String, Object>> getNextFlowElement(String currentTaskId) {
        TaskService taskService = processEngine.getTaskService();
        Task currentTask = taskService.createTaskQuery().taskId(currentTaskId).singleResult();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());

        List<FlowElement> nextFlowElements = new ArrayList<>();
        setNextFlowElements(nextFlowElements, bpmnModel, currentTask.getTaskDefinitionKey());

        List<Map<String, Object>> list = new ArrayList<>();

        for (FlowElement nextFlowElement : nextFlowElements) {
            Map<String, Object> map = new HashMap<>();
            map.put("activityId", nextFlowElement.getId());
            map.put("activityName", nextFlowElement.getName());
            list.add(map);
        }
        return list;
    }

    /**
     * 获取下一个任务节点
     *
     * @param nextFlowElements
     * @param bpmnModel
     * @param key
     */
    private void setNextFlowElements(List<FlowElement> nextFlowElements, BpmnModel bpmnModel, String key) {
        FlowElement currentFlowElement = bpmnModel.getFlowElement(key);
        if (currentFlowElement instanceof FlowNode) {
            List<SequenceFlow> outgoingFlows = ((FlowNode) currentFlowElement).getOutgoingFlows();

            for (SequenceFlow sequenceFlow : outgoingFlows) {
                FlowElement targetFlowElement = bpmnModel.getFlowElement(sequenceFlow.getTargetRef());

                // Exclude Gateway
                if (targetFlowElement instanceof UserTask) {
                    nextFlowElements.add(targetFlowElement);
                } else {
                    System.out.println("---->" + targetFlowElement.getClass());
                    setNextFlowElements(nextFlowElements, bpmnModel, targetFlowElement.getId());
                }
            }
        }
    }

    public List<Map<String, String>> getAllFlowElement(String taskId, Map<String, Object> data) {
        TaskService taskService = processEngine.getTaskService();
        Task currentTask = taskService.createTaskQuery().taskId(taskId).singleResult();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());

        Map<String, String> line = getLine(bpmnModel);

        List<Map<String, String>> nextFlowElements = new ArrayList<>();

        getAllFlowElements(data, line, nextFlowElements, bpmnModel, "start");
        return nextFlowElements;
    }


    public List<Map<String, String>> getAllFlowElementByKey(String processKey, Map<String, Object> data) {

        ProcessDefinition processDefinition = getLatestProcessDefinition(processKey);

        RepositoryService repositoryService = processEngine.getRepositoryService();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        Map<String, String> line = getLine(bpmnModel);

        List<Map<String, String>> nextFlowElements = new ArrayList<>();

        getAllFlowElements(data, line, nextFlowElements, bpmnModel, "start");
        return nextFlowElements;
    }

    /**
     * 获取所有线条
     *
     * @param bpmnModel
     * @return
     */
    private Map<String, String> getLine(BpmnModel bpmnModel) {
        Map<String, String> line = new HashMap<>();
        // 获取所有的流程定义
        Collection<Process> processes = bpmnModel.getProcesses();
        for (Process process : processes) {
            // 获取流程中的所有流程元素，包括任务、网关、事件等
            Collection<FlowElement> flowElements = process.getFlowElements();

            // 遍历流程元素，找到连线
            for (FlowElement flowElement : flowElements) {
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    line.put(sequenceFlow.getSourceRef(), sequenceFlow.getConditionExpression());
                    line.put(sequenceFlow.getTargetRef(), sequenceFlow.getConditionExpression());
                    line.put(sequenceFlow.getSourceRef() + "<-->" + sequenceFlow.getTargetRef(), sequenceFlow.getConditionExpression());
                }
            }
        }
        return line;
    }

    private void getAllFlowElements(Map<String, Object> data, Map<String, String> line, List<Map<String, String>> flowElements, BpmnModel bpmnModel, String key) {
        FlowElement currentFlowElement = bpmnModel.getFlowElement(key);
        if (currentFlowElement instanceof FlowNode) {

            Map<String, String> allKey = new HashMap<>();
            allKey.put("name", currentFlowElement.getName());
            allKey.put("key", currentFlowElement.getId());
            if (currentFlowElement instanceof UserTask) {
                allKey.put("type", "userTask");
            } else if (currentFlowElement instanceof ExclusiveGateway) {
                allKey.put("type", "gateway");
            } else if (currentFlowElement instanceof StartEvent) {
                allKey.put("type", "start");
            } else if (currentFlowElement instanceof EndEvent) {
                allKey.put("type", "end");
            } else {
                allKey.put("type", "other");
            }
            System.out.println(currentFlowElement);
            flowElements.add(allKey);
            List<SequenceFlow> outgoingFlows = ((FlowNode) currentFlowElement).getOutgoingFlows();
            for (SequenceFlow sequenceFlow : outgoingFlows) {
                FlowElement targetFlowElement = bpmnModel.getFlowElement(sequenceFlow.getTargetRef());
                String name = line.get(key + "<-->" + targetFlowElement.getId());
                if (name != null && !XmlUtil.expression(name, data)) {
                    continue;
                }
                Map<String, String> lineMap = new HashMap<>();
                lineMap.put("source", currentFlowElement.getId());
                lineMap.put("type", "line");
                lineMap.put("target", targetFlowElement.getId());
                flowElements.add(lineMap);
                getAllFlowElements(data, line, flowElements, bpmnModel, targetFlowElement.getId());
            }
        }
    }

    public List<Map<String, Object>> getActivityInstance(String processInstanceId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        if (tasks.size() == 0) {
            return null;
        }
        String taskName = null;
        Date taskTime = null;

        for (Task task : tasks) {
            taskName = task.getName();
            taskTime = task.getCreateTime();
            Map<String, Object> map = new HashMap<>();
            map.put("name", task.getName());
            map.put("id", task.getId());
            map.put("assignee", task.getAssignee());
            map.put("createTime", task.getCreateTime());
            list.add(map);
        }
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(tasks.get(0).getId()).stream()
                .filter(link -> "candidate".equals(link.getType()) && link.getGroupId() != null).toList();
        if (identityLinks != null && identityLinks.size() > 0) {
            //当有组有数据时 是显示组
            list.clear();
        }
        for (IdentityLink link : identityLinks) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", taskName);
            map.put("group", link.getGroupId());
            map.put("id", link.getTaskId());
            map.put("createTime", taskTime);
            list.add(map);
        }
        return list;
    }

    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) {
        try {
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            //流程走完的不显示图
            if (pi == null) {
                return;
            }
            Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
            //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
            String InstanceId = task.getProcessInstanceId();
            List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(InstanceId).list();

            //得到正在执行的Activity的Id
            List<String> activityIds = new ArrayList<>();
            List<String> flows = new ArrayList<>();
            for (Execution exe : executions) {
                List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
                activityIds.addAll(ids);
            }

            //获取流程图
            BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
            ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
            ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
            InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, true);
            OutputStream out = null;
            byte[] buf = new byte[1024];
            int legth = 0;
            try {
                out = httpServletResponse.getOutputStream();
                while ((legth = in.read(buf)) != -1) {
                    out.write(buf, 0, legth);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

        } catch (Exception e) {

        }
    }

    public void contextLoads() {
        UserEntityImpl user = new UserEntityImpl();
        user.setId("admin");
        user.setDisplayName("江南一点雨");
        user.setPassword("admin");
        user.setFirstName("java");
        user.setLastName("boy");
        user.setEmail("javaboy@qq.com");
        user.setRevision(0);
        identityService.saveUser(user);
    }

}
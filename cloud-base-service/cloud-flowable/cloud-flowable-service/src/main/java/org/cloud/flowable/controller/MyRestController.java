package org.cloud.flowable.controller;

import org.cloud.flowable.service.MyService;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class MyRestController {
    @Autowired
    private MyService myService;


    @PostMapping(value = "/createDeployment")
    public void createDeployment(String resourceName, String bpmnXmlStr) {
        myService.createDeployment(resourceName, bpmnXmlStr);
    }

    @PostMapping(value = "/delete")
    public void delete(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        myService.deleteDeployment(deployId);
    }

    @PostMapping(value = "/getDeployment")
    public List<Deployment> getDeployment() {
        return myService.getDeployment();
    }

    @PostMapping(value = "/process")
    public void startProcessInstance(String processInstanceById) {
        myService.startProcess(processInstanceById);
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
        List<Task> tasks = myService.getTasks(assignee);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }

    static class TaskRepresentation {

        private String id;
        private String name;

        public TaskRepresentation(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}

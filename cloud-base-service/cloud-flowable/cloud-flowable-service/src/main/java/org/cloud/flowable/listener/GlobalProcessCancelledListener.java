package org.cloud.flowable.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 流程结束全局监听器
 *
 * @author: gblfy
 * @date 2022-06-21
 */
@Component
public class GlobalProcessCancelledListener extends AbstractFlowableEngineEventListener {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private ProcessEngine processEngine;

    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        logger.info("进入流程实例被取消监听器------------------------Start---------------------->");



        String eventName = event.getType().name();

        FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) event;
        ExecutionEntityImpl processInstance = (ExecutionEntityImpl) flowableEntityEvent.getEntity();

        Date startTime = processInstance.getStartTime();
        String processDefinitionKey = processInstance.getProcessDefinitionKey();
        String processInstanceId = processInstance.getProcessInstanceId();
        String processInstanceBusinessKey = processInstance.getProcessInstanceBusinessKey();
        int suspensionState = processInstance.getSuspensionState();


        logger.info("流程事件类型->{}", eventName);
        logger.info("流程开始时间->{}", startTime);
        logger.info("流程定义Key->{}", processDefinitionKey);
        logger.info("流程实例ID->{}", processInstanceId);
        logger.info("流程业务key->{}", processInstanceBusinessKey);
        logger.info("流程是否挂起标志->{}", suspensionState);


        logger.info("进入流程实例被取消监听器------------------------End---------------------->");
    }

}
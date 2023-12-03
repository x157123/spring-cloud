package org.cloud.flowable.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Flowable全局监听配置
 * 用途:在任务特殊节点或者流程的特殊节点做一些自定义操作
 *
 * @author gblfy
 * @Date 2022-06-21
 **/
@Configuration
public class FlowableGlobListenerConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private SpringProcessEngineConfiguration configuration;

    @Autowired
    private GlobalProcessStartedListener globalProcessStartedListener;

    @Autowired
    private GlobalProcessEndListener globalProcessEndListener;
    @Autowired
    private GlobalProcessDelListener globalProcessDelListener;
    @Autowired
    private GlobalProcessCancelledListener globalProcessCancelledListener;

    @Autowired
    private CustomFlowableEventListener customFlowableEventListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        
        FlowableEventDispatcher dispatcher = configuration.getEventDispatcher();

        //流程开始全局监听
        dispatcher.addEventListener(globalProcessStartedListener, FlowableEngineEventType.PROCESS_STARTED);

        //流程结束全局监听
        dispatcher.addEventListener(globalProcessEndListener, FlowableEngineEventType.PROCESS_COMPLETED);

        //流程删除全局监听
        dispatcher.addEventListener(globalProcessDelListener, FlowableEngineEventType.ENTITY_DELETED);

        //流程实例被取消
        dispatcher.addEventListener(globalProcessCancelledListener, FlowableEngineEventType.PROCESS_CANCELLED);

//        dispatcher.addEventListener(customFlowableEventListener);
    }

}
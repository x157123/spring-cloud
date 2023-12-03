package org.cloud.flowable.listener;

import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEventType;
import org.springframework.stereotype.Component;

@Component
public class CustomFlowableEventListener implements FlowableEventListener {

    @Override
    public void onEvent(FlowableEvent event) {
        FlowableEventType eventType = event.getType();
        System.out.println("---------------" + eventType);
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}

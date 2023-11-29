package org.cloud.flowable.listener;

import org.flowable.common.engine.api.Engine;
import org.flowable.common.engine.api.engine.EngineLifecycleListener;

public class StartEndEngineLifecycleListener implements EngineLifecycleListener {


    @Override
    public void onEngineBuilt(Engine engine) {
        System.out.println("流程引擎启动时的自定义逻辑");
    }

    @Override
    public void onEngineClosed(Engine engine) {
        System.out.println("流程引擎关闭时的自定义逻辑");
    }
}

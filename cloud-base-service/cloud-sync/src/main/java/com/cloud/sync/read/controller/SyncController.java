package com.cloud.sync.read.controller;

import com.cloud.sync.param.SyncConfigParam;
import com.cloud.sync.read.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 */
@Controller
public class SyncController {

    String str = "{\"serveId\":0,\"name\":\"xxxxxxxxxxxx\",\"readerDb\":\"1\",\"writerDb\":\"2\",\"associateTable\":[{\"readerTable\":\"gp_area_info\",\"type\":\"1\",\"writerTable\":\"gp_area_infos\",\"associateColumn\":[{\"readerColumn\":\"id\",\"writerTable\":\"id\"},{\"readerColumn\":\"org_code\",\"writerTable\":\"org_code\"},{\"readerColumn\":\"grid_area\",\"writerTable\":\"grid_area\"},{\"readerColumn\":\"create_date\",\"writerTable\":\"create_date\"},{\"readerColumn\":\"update_date\",\"writerTable\":\"update_date\"}]}]}";

    @Autowired
    private SyncService syncService;

    @GetMapping("/begin")
    @ResponseBody
    public void begin(Long connectId) {
        syncService.begin(connectId);
    }

    @GetMapping("/stop")
    @ResponseBody
    public void stop(Long connectId) {
        syncService.stop(connectId);
    }


    /**
     * 重置采集 从新开始
     *
     * @param connectId
     */
    @GetMapping("/repeat")
    @ResponseBody
    public void repeat(Long connectId) {
        syncService.repeat(connectId);
    }

    @GetMapping("/msg")
    @ResponseBody
    public void msg() {
        syncService.msg();
    }

    @GetMapping("/saveSyncConfig")
    @ResponseBody
    public void saveSyncConfig(@RequestBody SyncConfigParam syncConfig) {
        syncService.saveSyncConfig(syncConfig);
        System.out.println(syncConfig);
    }
}

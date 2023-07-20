package com.cloud.sync.controller;

import com.cloud.sync.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 */
@Controller
public class SyncController {


    @Autowired
    private SyncService syncService;

    @GetMapping("/begin")
    @ResponseBody
    public void begin(Long connectId) {
        syncService.begin(connectId);
    }

    @GetMapping("/start")
    @ResponseBody
    public void start(Long connectId) {
        syncService.start(connectId);
    }


    @GetMapping("/stop")
    @ResponseBody
    public void stop(Long connectId) {
        syncService.stop(connectId);
    }


    @GetMapping("/msg")
    @ResponseBody
    public void msg() {
        syncService.msg();
    }
}

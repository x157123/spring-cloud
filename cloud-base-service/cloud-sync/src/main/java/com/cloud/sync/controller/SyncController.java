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
    public void begin(){
        syncService.begin();
    }

    @GetMapping("/start")
    @ResponseBody
    public void start(){
        syncService.start();
    }


    @GetMapping("/stop")
    @ResponseBody
    public void stop(){
        syncService.stop();
    }


    @GetMapping("/msg")
    @ResponseBody
    public void msg(){
        syncService.msg();
    }
}

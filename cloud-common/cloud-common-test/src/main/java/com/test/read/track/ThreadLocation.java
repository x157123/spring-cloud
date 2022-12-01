package com.test.read.track;

import com.cloud.common.util.http.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ThreadLocation extends Thread {
    private Thread t;
    private String threadName;

    ThreadLocation(String name) {
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        Random ran = new Random(); //创建一个随机产生数类Scanner
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(threadName + ":" +sdf.format(new Date()));
        for (int i = 0; i < 10000; i++) {
            int id = ran.nextInt(3001) + 3000; //随机产生0-300的数
            OkHttpUtils.builder().url("http://127.0.0.1:1881/gridInspectionTrack/save?lat=90.33344&lon=12.34444&inspectionId=" + id)
                    .post(true)
                    .sync();
            if (i % 1000 == 0) {
                System.out.println(threadName + ":" + i);
            }
        }
        System.out.println(threadName + ":" +sdf.format(new Date()));
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
package com.cloud.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author liulei
 */

@SpringBootApplication
public class SyncApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SyncApplication.class, args);
//        JdbcOffsetBackingStore.setAc(applicationContext);
    }
}

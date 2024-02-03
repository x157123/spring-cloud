package com.cloud.sync.read.storage;

import com.alibaba.fastjson2.JSONObject;
import com.cloud.sync.param.ServeConfigParam;
import com.cloud.sync.base.service.ServeConfigService;
import com.cloud.sync.vo.ServeConfigVo;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.apache.kafka.connect.util.Callback;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author liulei
 */
public class JdbcOffsetBackingStore implements OffsetBackingStore {

    protected Map<ByteBuffer, ByteBuffer> data = new HashMap<>();

    protected ExecutorService executor;

    protected Long serveId;

    private static ConfigurableApplicationContext ac;

    public static void setAc(ConfigurableApplicationContext applicationContext) {
        ac = applicationContext;
    }

    @Override
    public void start() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(this.getClass().getSimpleName() + "-%d").build();
        executor = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdown();
            // Best effort wait for any get() and set() tasks (and caller's callbacks) to complete.
            try {
                executor.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!executor.shutdownNow().isEmpty()) {
                throw new ConnectException("Failed to stop MemoryOffsetBackingStore. Exiting without cleanly " +
                        "shutting down pending tasks and/or callbacks.");
            }
            executor = null;
        }
    }

    @Override
    public Future<Map<ByteBuffer, ByteBuffer>> get(Collection<ByteBuffer> collection) {
//        System.out.println("------------->:get()");
        //读取数据库数据
        return executor.submit(() -> {
            Map<ByteBuffer, ByteBuffer> result = new HashMap<>();
            for (ByteBuffer key : collection) {
                result.put(key, data.get(key));
            }
            return result;
        });
    }

    @Override
    public Future<Void> set(Map<ByteBuffer, ByteBuffer> map, Callback<Void> callback) {
//        System.out.println("------------->:set()");
        //写入数据库
        return executor.submit(() -> {
            for (Map.Entry<ByteBuffer, ByteBuffer> entry : map.entrySet()) {
                data.put(entry.getKey(), entry.getValue());
                {
                    // 打印游标
                    Charset charset = Charset.forName("utf-8");
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer keyBuffer = decoder.decode(entry.getKey());
                    CharBuffer valueBuffer = decoder.decode(entry.getValue());
//                    System.out.println("key-->:" + keyBuffer.toString());
//                    System.out.println("value-->:" + valueBuffer.toString());
                    //写入数据库
                    save(keyBuffer.toString(), valueBuffer.toString());
                }
            }
            if (callback != null) {
                callback.onCompletion(null, null);
            }
            return null;
        });
    }

    @Override
    public Set<Map<String, Object>> connectorPartitions(String s) {
        return null;
    }

    @Override
    public void configure(WorkerConfig workerConfig) {
        String str = workerConfig.originals().get("database.server.id").toString();
        serveId = Long.parseLong(str);
        load();
    }

    /**
     * 获取数据库
     */
    private void load() {
        ServeConfigService serveConfigService = ac.getBean(ServeConfigService.class);
        if (serveConfigService != null) {
            ServeConfigVo serveConfigVo = serveConfigService.findByServerId(serveId);
            if (serveConfigVo != null) {
                String offset = serveConfigVo.getOffSet();
                if (offset != null && offset.trim().length() > 0) {
                    JSONObject jsonObject = JSONObject.parseObject(offset);
                    if (jsonObject.getString("key") != null && jsonObject.getString("value") != null) {
                        data.put(ByteBuffer.wrap(jsonObject.getString("key").getBytes()), ByteBuffer.wrap(jsonObject.getString("value").getBytes()));
                    }
                }
            }
        }
    }

    /**
     * 写入数据库
     */
    private void save(String key, String value) {
        ServeConfigService serveConfigService = ac.getBean(ServeConfigService.class);
        if (serveConfigService != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", key);
            jsonObject.put("value", value);
            ServeConfigParam serveConfigParam = new ServeConfigParam();
            serveConfigParam.setServeId(serveId);
            serveConfigParam.setOffSet(jsonObject.toString());
            serveConfigService.save(serveConfigParam);
        }
    }
}

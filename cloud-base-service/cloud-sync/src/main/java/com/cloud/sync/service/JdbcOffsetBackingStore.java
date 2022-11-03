package com.cloud.sync.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.apache.kafka.connect.util.Callback;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author liulei
 */
public class JdbcOffsetBackingStore implements OffsetBackingStore {

    protected Map<ByteBuffer, ByteBuffer> data = new HashMap<>();

    protected ExecutorService executor;

    @Override
    public void start() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(this.getClass().getSimpleName() + "-%d").build();
        executor = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        load();
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

                    System.out.println("key-->:" + keyBuffer.toString());
                    System.out.println("value-->:" + valueBuffer.toString());
                }
            }
            //写入数据库
            save();
            if (callback != null) {
                callback.onCompletion(null, null);
            }
            return null;
        });
    }

    @Override
    public void configure(WorkerConfig workerConfig) {
    }

    /**
     * 获取数据库
     */
    private void load(){
        String key = "{\"schema\":null,\"payload\":[\"engine\",{\"server\":\"my-app-connector\"}]}";
        String value = "{\"ts_sec\":1667444955,\"file\":\"mysql-bin.000090\",\"pos\":195,\"gtids\":\"6e743415-ca40-11e8-8a55-10f0059aec86:1-17557\"}";
        data.put(ByteBuffer.wrap(key.getBytes()),ByteBuffer.wrap(value.getBytes()));
    }

    /**
     * 写入数据库
     */
    private void save(){

    }
}

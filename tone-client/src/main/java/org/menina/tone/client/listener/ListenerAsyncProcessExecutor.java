package org.menina.tone.client.listener;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
public class ListenerAsyncProcessExecutor {

    private static int maxTaskNum = 100;

    private static int corePoolSize = 1;

    private static int maxPoolSize = 5;

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Tone-Async-Notify-Thread-%d").build();

    private static ExecutorService executor = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(maxTaskNum),
            namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy()
            );

    public static void commit(final ListenerChain chain, final Map<String, String> data){
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        chain.invoke(data);
                    }catch (Throwable t){
                        ///  防御性容错
                        log.error(t.getMessage(), t);
                    }
                }
            });
        }catch (RejectedExecutionException e){
            log.error(String.format("listener process executor has exhausted, %s"), e.getMessage(), e);
        }catch (Throwable t){
            log.error(String.format("Commit listener to process failed, %s"), t.getMessage(), t);
        }

        return;
    }
}

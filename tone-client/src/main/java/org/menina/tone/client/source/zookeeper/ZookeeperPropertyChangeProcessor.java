package org.menina.tone.client.source.zookeeper;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.menina.tone.client.hotload.HotLoading;
import org.menina.tone.client.hotload.HotLoadingProcessor;
import org.menina.tone.client.listener.ListenerAsyncProcessExecutor;
import org.menina.tone.client.listener.ListenerChainFactory;
import org.menina.tone.client.source.PropertyChangeProcessor;
import org.menina.tone.client.source.ResourceLoader;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
@NoArgsConstructor
public class ZookeeperPropertyChangeProcessor implements PropertyChangeProcessor {

    private static int maxTaskNum = 100;

    private static int corePoolSize = 1;

    private static int maxPoolSize = 5;

    private HotLoading hotLoading = new HotLoadingProcessor();

    private ZookeeperResourceLoader resourceLoader;

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Tone-Properties-Reload-Thread-%d").build();

    private static ExecutorService executor = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(maxTaskNum),
            namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy()
    );

    public ZookeeperPropertyChangeProcessor(ZookeeperResourceLoader zookeeperResourceLoader){
        this.resourceLoader = zookeeperResourceLoader;
    }

    @Override
    public Map<String, String> reloadProperty(String nodePath) {
        final Map<String, String> data = resourceLoader.loadProperty(nodePath);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    try{
                        hotLoading.reload(entry.getKey(), entry.getValue());
                    }catch (Throwable t){
                        ///  防御性容错
                        log.error(t.getMessage(), t);
                    }
                }
            }
        });

        return data;
    }

    @Override
    public void notifier(Map<String, String> data) {
        ListenerAsyncProcessExecutor.commit(new ListenerChainFactory().getListenerChain(), data);
    }
}

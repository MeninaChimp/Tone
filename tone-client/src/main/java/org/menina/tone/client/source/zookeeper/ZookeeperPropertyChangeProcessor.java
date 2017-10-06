package org.menina.tone.client.source.zookeeper;

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

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
@NoArgsConstructor
public class ZookeeperPropertyChangeProcessor implements PropertyChangeProcessor {

    private HotLoading hotLoading = new HotLoadingProcessor();

    private ZookeeperResourceLoader resourceLoader;

    private static ExecutorService executor = Executors.newFixedThreadPool(5);

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

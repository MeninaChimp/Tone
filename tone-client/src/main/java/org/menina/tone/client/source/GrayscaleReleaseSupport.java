package org.menina.tone.client.source;

/**
 * Created by Menina on 2017/10/4.
 * 灰度发布支持
 */
public interface GrayscaleReleaseSupport {

    /**
     * 作用于事件通知的配置仓库
     * @param key
     */
    void subscribe(String key);

    /**
     * 作用于非事件通知的配置仓库
     * @param key
     */
    void notifier(String key);
}

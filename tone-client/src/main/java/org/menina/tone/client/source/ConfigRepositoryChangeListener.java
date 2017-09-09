package org.menina.tone.client.source;

/**
 * 配置仓库变更监听器，对于非事件通知的配置仓库，需自行监听配置变更。
 * 当变更发生，需自行实现配置仓库配置的reload
 *
 * Created by Menina on 2017/9/9.
 */
public interface ConfigRepositoryChangeListener {

    void start();

    void onchange();
}

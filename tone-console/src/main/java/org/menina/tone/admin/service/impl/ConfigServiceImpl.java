package org.menina.tone.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.joda.time.DateTime;
import org.menina.tone.admin.service.ConfigService;
import org.menina.tone.common.utils.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by menina on 2017/9/23.
 */
@Slf4j
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    private CuratorFramework client;

    @Value("${tone.zookeeper.address}")
    private String zookeeperAddress;

    @Override
    public void initialZookeeperClient(String url) {
        this.client = CuratorFrameworkFactory.builder()
                .connectString(this.zookeeperAddress)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        this.client.start();
    }

    @PostConstruct
    public void init() {
        this.initialZookeeperClient(null);
    }

    @Override
    public String getData(String path) throws Exception {
        try {
            return new String(this.client.getData().forPath(path));
        } catch (KeeperException.NoNodeException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<String> nodes(String parent) throws Exception {
        try {
            GetChildrenBuilder childrenBuilder = this.client.getChildren();
            return childrenBuilder.forPath(parent);
        }catch (KeeperException.NoNodeException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public void save(String path, String value) throws Exception {
        this.client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, value.getBytes());
    }

    @Override
    public void update(String path, String value) throws Exception {
        this.client.setData().forPath(path, value.getBytes());
        String updateTime = path + Constant.UPDATE_TIME;
        if (this.getData(updateTime) == null) {
            this.client.create().withMode(CreateMode.PERSISTENT).forPath(updateTime, new DateTime().toString().getBytes());
            return;
        }

        this.client.setData().forPath(updateTime, new DateTime().toString().getBytes());
    }

    @Override
    public void removeNode(String path) throws Exception {
        this.client.delete().deletingChildrenIfNeeded().forPath(path);
    }

    @Override
    public void grayRelease(String path, String value) throws Exception {
        this.client.setData().forPath(path, value.getBytes());
    }
}

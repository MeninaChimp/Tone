package org.menina.tone.admin.service;

import java.util.List;

/**
 * Created by menina on 2017/9/23.
 */
public interface ConfigService {

    void initialZookeeperClient(String url);

    String getData(String path) throws Exception;

    List<String> nodes(String parent) throws Exception;

    void save(String path, String value) throws Exception;

    void update(String path, String value) throws Exception;

    void removeNode(String path) throws Exception;

    void grayRelease(String path, String value) throws Exception;
}

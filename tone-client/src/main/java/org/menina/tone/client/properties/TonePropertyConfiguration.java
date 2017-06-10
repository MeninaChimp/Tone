package org.menina.tone.client.properties;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.curator.utils.ZKPaths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * Created by Menina on 2017/6/10.
 */
@Getter
@Configuration
@PropertySource(value = "classpath:tone.properties", ignoreResourceNotFound = true)
public class TonePropertyConfiguration {

    @Value("${address}")
    private String address;

    @Value("${root}")
    private String root;

    @Value("${version}")
    private String version;

    @Value("${nodes}")
    private String nodes;

    public List<String> makePaths(){
        String[] nodesArr = nodes.trim().split(",");
        List<String> paths = Lists.newArrayList();
        for(String node : nodesArr){
            paths.add(ZKPaths.makePath(ZKPaths.makePath(root, version), node));
        }

        return paths;
    }
}

package org.menina.tone.client.properties;

import com.google.common.collect.Lists;
import org.apache.curator.utils.ZKPaths;

import java.util.List;

/**
 * Created by Menina on 2017/6/10.
 */
public class TonePropertyConfiguration {

    private static String address;

    private static String root;

    private static String version;

    private static String nodes;

    private static String resourceLoader;

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        TonePropertyConfiguration.address = address;
    }

    public static String getRoot() {
        return root;
    }

    public static void setRoot(String root) {
        TonePropertyConfiguration.root = root;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        TonePropertyConfiguration.version = version;
    }

    public static String getNodes() {
        return nodes;
    }

    public static void setNodes(String nodes) {
        TonePropertyConfiguration.nodes = nodes;
    }

    public static String getResourceLoader() {
        return resourceLoader;
    }

    public static void setResourceLoader(String resourceLoader) {
        TonePropertyConfiguration.resourceLoader = resourceLoader;
    }

    public List<String> makePaths() {
        String[] nodesArr = nodes.trim().split(",");
        List<String> paths = Lists.newArrayList();
        for (String node : nodesArr) {
            paths.add(ZKPaths.makePath(ZKPaths.makePath(root, version), node));
        }

        return paths;
    }
}

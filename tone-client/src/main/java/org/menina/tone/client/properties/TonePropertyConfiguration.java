package org.menina.tone.client.properties;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.utils.ZKPaths;
import org.menina.tone.client.source.ConfigRepositoryChangeListener;
import org.menina.tone.client.source.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created by Menina on 2017/6/10.
 */
@Slf4j
@Data
public class TonePropertyConfiguration {

    private static final String CONFIG_FILE = "tone.properties";
    private static final String CONNECT_STR = "tone.connectAddress";
    private static final String ROOT_NODE = "tone.rootNode";
    private static final String VERSION = "tone.version";
    private static final String NODES = "tone.nodes";

    private String address;

    private String root;

    private String version;

    private String nodes;

    private String resourceLoader;

    private String configRepositoryChangeListener;

    public TonePropertyConfiguration(){
        super();
        Properties props = new Properties();
        try {
            Enumeration<URL> registerFiles = TonePropertyConfiguration.class.getClassLoader().getResources(
                    CONFIG_FILE);

            URL registerFile = null;
            while (registerFiles.hasMoreElements()) {
                registerFile = registerFiles.nextElement();
                try (InputStream in = registerFile.openStream()) {
                    props.load(in);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        this.address = props.getProperty(CONNECT_STR);
        this.root = props.getProperty(ROOT_NODE);
        this.version = props.getProperty(VERSION);
        this.nodes = props.getProperty(NODES, "");
    }

    public static TonePropertyConfiguration instance = new TonePropertyConfiguration();

    public static TonePropertyConfiguration getInstance(){
        return instance;
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

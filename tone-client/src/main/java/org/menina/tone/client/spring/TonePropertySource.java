package org.menina.tone.client.spring;

import org.springframework.core.env.PropertySource;

import java.util.Map;

/**
 * Created by Menina on 2017/6/11.
 */
public class TonePropertySource extends PropertySource<Map<String, String>>{

    public static final String name = "TONE_PROPERTY_SOURCE";

    public TonePropertySource(String name, Map<String, String> source) {
        super(name, source);
    }

    public TonePropertySource(String name) {
        super(name);
    }

    @Override
    public Object getProperty(String name) {
        return this.getSource().get(name);
    }
}

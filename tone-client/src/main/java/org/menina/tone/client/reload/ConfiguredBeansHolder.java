package org.menina.tone.client.reload;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Menina on 2017/6/10.
 */
public class ConfiguredBeansHolder {

    private static Map<String, Set<String>> configuredBeanNamesMap = new HashMap<>();

    public static Set<String> getConfiguredBeans(String propertyName){
        return configuredBeanNamesMap.get(propertyName);
    }

    public static void recordConfiguredBean(String propertyName, String beanName){
        if(!configuredBeanNamesMap.containsKey(propertyName)){
            configuredBeanNamesMap.put(propertyName,  new HashSet<String>());
        }

        configuredBeanNamesMap.get(propertyName).add(beanName);
    }
}

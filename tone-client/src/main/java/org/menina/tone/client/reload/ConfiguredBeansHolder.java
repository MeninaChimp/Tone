package org.menina.tone.client.reload;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Menina on 2017/6/10.
 */
public class ConfiguredBeansHolder {

    private static Map<String, Map<String, Set<String>>> placeholderBeanNamesMap = new HashMap<>();

    public static Map<String, Set<String>> getConfiguredBeans(String placeholder){
        return placeholderBeanNamesMap.get(placeholder);
    }

    public static void recordConfiguredBean(String placeholder, String propertyName, String beanName){
        if(!placeholderBeanNamesMap.containsKey(placeholder)){
            placeholderBeanNamesMap.put(placeholder, new HashMap<String, Set<String>>());
        }

        if(!placeholderBeanNamesMap.get(placeholder).containsKey(propertyName)){
            placeholderBeanNamesMap.get(placeholder).put(propertyName, new HashSet<String>());
        }

        placeholderBeanNamesMap.get(placeholder).get(propertyName).add(beanName);
    }
}

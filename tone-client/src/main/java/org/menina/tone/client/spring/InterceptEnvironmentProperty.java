package org.menina.tone.client.spring;

import org.menina.tone.client.properties.TonePropertyConfiguration;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by Menina on 2017/6/12.
 */
public class InterceptEnvironmentProperty implements Condition{
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        TonePropertyConfiguration.setAddress(context.getEnvironment().getProperty("address"));
        TonePropertyConfiguration.setNodes(context.getEnvironment().getProperty("nodes"));
        TonePropertyConfiguration.setRoot(context.getEnvironment().getProperty("root"));
        TonePropertyConfiguration.setVersion(context.getEnvironment().getProperty("version"));
        TonePropertyConfiguration.setResourceLoader(context.getEnvironment().getProperty("resourceLoader"));
        return false;
    }
}

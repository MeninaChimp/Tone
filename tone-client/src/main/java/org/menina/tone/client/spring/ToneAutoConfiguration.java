package org.menina.tone.client.spring;

import org.menina.tone.client.listener.DefaultPropertyChangeListener;
import org.menina.tone.client.listener.Listener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Menina on 2017/6/10.
 */
@Configuration
public class ToneAutoConfiguration {

    @Bean
    public Listener defaultListener(){
        return new DefaultPropertyChangeListener();
    }
}

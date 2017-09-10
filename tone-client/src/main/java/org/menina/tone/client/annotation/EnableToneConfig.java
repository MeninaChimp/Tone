package org.menina.tone.client.annotation;

import org.menina.tone.client.spring.ToneConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Menina on 2017/9/10.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ToneConfigRegistrar.class)
public @interface EnableToneConfig {
}

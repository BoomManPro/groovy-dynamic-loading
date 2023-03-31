package com.boommanpro.groovydynamicloading.impl;

import com.boommanpro.groovydynamicloading.ApplicationContextUtils;
import com.boommanpro.groovydynamicloading.GlueFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * copy from xxl-job
 */
@Slf4j
public class SpringGlueFactory extends GlueFactory {


    /**
     * inject action of spring
     * @param instance
     */
    @Override
    public void injectService(Object instance){
        if (instance==null) {
            return;
        }

        if (ApplicationContextUtils.getApplicationContext() == null) {
            return;
        }

        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Object fieldBean = null;
            // with bean-id, bean could be found by both @Resource and @Autowired, or bean could only be found by @Autowired

            if (AnnotationUtils.getAnnotation(field, Resource.class) != null) {
                try {
                    Resource resource = AnnotationUtils.getAnnotation(field, Resource.class);
                    if (resource.name()!=null && resource.name().length()>0){
                        fieldBean = ApplicationContextUtils.getApplicationContext().getBean(resource.name());
                    } else {
                        fieldBean = ApplicationContextUtils.getApplicationContext().getBean(field.getName());
                    }
                } catch (Exception e) {
                }
                if (fieldBean==null ) {
                    fieldBean = ApplicationContextUtils.getApplicationContext().getBean(field.getType());
                }
            } else if (AnnotationUtils.getAnnotation(field, Autowired.class) != null) {
                Qualifier qualifier = AnnotationUtils.getAnnotation(field, Qualifier.class);
                if (qualifier!=null && qualifier.value()!=null && qualifier.value().length()>0) {
                    fieldBean = ApplicationContextUtils.getApplicationContext().getBean(qualifier.value());
                } else {
                    fieldBean = ApplicationContextUtils.getApplicationContext().getBean(field.getType());
                }
            }

            if (fieldBean!=null) {
                field.setAccessible(true);
                try {
                    field.set(instance, fieldBean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}

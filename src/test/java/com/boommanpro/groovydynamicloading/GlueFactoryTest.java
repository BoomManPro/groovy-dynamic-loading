package com.boommanpro.groovydynamicloading;


import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class GlueFactoryTest {


    @Test
    public void createNewClassTest() throws Exception {
        ClassPathResource resource = new ClassPathResource("HelloWorldServiceImpl.java");
        GlueFactory glueFactory = GlueFactory.getInstance();
        HelloWorldService helloWorldService = glueFactory.loadNewInstance(IOUtils.toString(resource.getInputStream()), HelloWorldService.class);
        Assertions.assertEquals("java-glud", helloWorldService.sayHello());
    }


}
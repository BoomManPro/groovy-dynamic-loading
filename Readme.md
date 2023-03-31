## Java Dynamic loading

e.g.
```java
    @Test
    public void createNewClassTest() throws Exception {
        ClassPathResource resource = new ClassPathResource("HelloWorldServiceImpl.java");
        GlueFactory glueFactory = GlueFactory.getInstance();
        HelloWorldService helloWorldService = glueFactory.loadNewInstance(IOUtils.toString(resource.getInputStream()), HelloWorldService.class);
        Assertions.assertEquals("java-glud", helloWorldService.sayHello());
    }
```
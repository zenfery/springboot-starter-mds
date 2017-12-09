# MDS : springboot-starter-mds

## 1. Overview - 概述
------------------------

Springboot multiple datasource.

基于 Springboot 的多数据源支持。

## 2. User Guide - 使用指南
--------------------------

### Get Package - 获取安装包

Package From Source - 从源码进行构建

- Clone or Download From Github 从Github上下载或克隆源码到本地电脑
``` bash
git clone https://github.com/zenfery/springboot-starter-mds.git
or
git clone -b 0.1 https://github.com/zenfery/springboot-starter-mds.git
```
- Use maven to package 使用 Maven 构建
``` bash
cd springboot-starter-mds
mvn clean install -Dmaven.test.skip=true
```
You can find the result jar in `mds/target` after build. 构建完成后，jar包会存在于 `mds/target` 目录中。

### Config Jar - 配置 Jar 包
- 普通 Java 项目。直接将 jar 包配置至 classpath 中即可。
- Maven 项目。配置 dependency :
``` xml
  <dependency>
    <groupId>cc.zenfery</groupId>
    <artifactId>springboot-starter-mds</artifactId>
    <version>0.1-SNAPSHOT</version>
  </dependency>
```

### Config application.yml or application.properties - springboot 配置文件

配置项含义说明

| Config Item (配置项) | Description (描述) |
| ------------------- | ------------------|
| spring.datasource   | Default datasource (默认数据源配置) |
| spring.mds          | Multi datasource (更多的数据源配置) |
| spring.mds.enabled   | Whether enabled - 是否开启，默认 true 开启。 |
| spring.mds.datasources[].name   | Name of datasource - 指定数据源名称。 |


Config Example - 配置示例：
- application.yml
``` yaml
  spring:
    datasource:
      name: "mds"
      url: "jdbc:mysql://127.0.0.1:3306/mds"
      username: "root"
      password: "mysql"
      driver-class-name: "com.mysql.jdbc.Driver"
      validation-query: "select 1 from dual"
    mds:
      enabled: true
      datasources:
        -
          name: mds1
          url: "jdbc:mysql://127.0.0.1:3306/mds1"
          username: "root"
          password: "mysql"
          driver-class-name: "com.mysql.jdbc.Driver"
          validation-query: "select 1 from dual"
```

### Coding - 编写代码
If you use the default DataSource `mds`, nothing need to do for original code. If you want to use the DataSource of the setting `spring.mds.datasources`, use the Annotation `Mds` on class or method, Such as `@Mds("mds1")`; the default `mds` DataSource will be used when you set annotation like `@Mds()` or `@Mds("")`.

如果使用数据源 `mds` ,原来的代码将不需要做任何变动，保持原样即可；`springboot-starter-mds` 会帮你处理。如果使用`spring.mds.datasources` 指定的数据源，代码中需要在使用的 **类** 或 **方法** 上使用注解`@Mds("mds1")`；若使用`@Mds("")`或`@Mds()`，将会启用默认的数据源 `mds`。

- Use Annotaion @Mds - 使用注解@Mds
``` java
  @Service
  public class TestServiceImpl implements TestService {

      @Autowired
      private TestDao testDao;

      @Override
      public String test() {
          return testDao.test();
      }

      @Mds("mds1")
      @Override
      public String test1() {
          return testDao.test();
      }

  }
```

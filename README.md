# MDS : springboot-starter-mds

## 1. Overview - 概述

Springboot multiple datasource.

基于 Springboot 的多数据源支持。

Springboot 官方仅支持单数据源，当然这也是 Springboot 建议的最佳实践；但在实际使用中，因为各种原因，采用 Springboot 构建的微服务还是有多数据源的需求，所以本工具就是给 Springboot 使用者提供一个方便使用的多数据源组件。

## 2. User Guide - 使用指南

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

If you can not find the artifact from your private maven repository, please check your central proxy address; public repository group https://oss.sonatype.org/content/groups/public contains release and snapshot artifact.

如果搭建了 maven 私服，但是在私服中找不到相应的构建包，建议检查一下私服代理的中央仓库地址是否正确；公共仓库地址 https://oss.sonatype.org/content/groups/public 包含发布包和快照包。

### Config application.yml or application.properties - springboot 配置文件

配置项含义说明：

| Config Item (配置项) | Description (描述) |
| ------------------- | ------------------|
| spring.datasource   | Default datasource (默认数据源配置) |
| spring.mds          | Multi datasource (更多的数据源配置) |
| spring.mds.enabled   | Whether enabled - 是否开启，默认 true 开启。 |
| spring.mds.datasources[].name   | Name of datasource - 指定数据源名称。 |

spring.mds.datasources[] 所支持的配置项与 spring.datasource 所支持的保持一致。若要查看完整的配置项含义，请析 spring boot 官方提供的文档。

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
      tomcat:
        maxActive: 105
        testWhileIdle: true
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

- application.properties

    ``` bash
  spring.datasource.name=mds
  spring.datasource.url=jdbc:mysql://127.0.0.1:3306/mds
  spring.datasource.username=root
  spring.datasource.password=mysql
  spring.datasource.driver-class-name=com.mysql.jdbc.Driver
  spring.datasource.validation-query=select 1 from dual

  spring.mds.enabled=true
  spring.mds.datasources[0].name=mds1
  spring.mds.datasources[0].url=jdbc:mysql://127.0.0.1:3306/mds1
  spring.mds.datasources[0].username=root
  spring.mds.datasources[0].password=mysql
  spring.mds.datasources[0].driver-class-name=com.mysql.jdbc.Driver
  spring.mds.datasources[0].validation-query=select 1 from dual
  ```

### Coding - 编写代码
If the default DataSource `mds` is used, nothing need to do for original code. If you want to use the DataSource of the setting `spring.mds.datasources`, use the Annotation `Mds` on class or method, Such as `@Mds("mds1")`; the default `mds` DataSource will be used when you set annotation like `@Mds()` or `@Mds("")`. Both class and method set the `@Mds`, it will to use it on the method.

如果使用默认数据源 `mds` ,原来的代码将不需要做任何变动，保持原样即可；`springboot-starter-mds` 会帮你处理。如果使用`spring.mds.datasources` 指定的数据源，代码中需要在使用的 **类** 或 **方法** 上使用注解`@Mds("mds1")`；若使用`@Mds("")`或`@Mds()`，将会启用默认的数据源 `mds`。 若类 和 方法上均使用了 `@Mds`, 方法上的将会起作用。

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

- Use Customized Annotation - 使用自定义注解
  - Customized Annotation - 自定义注解
    ``` java
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Mds("mds1")
    public @interface Mds1DataSource {

    }
    ```
  - Use in Code
    ``` java
    @Mds1DataSource
    @Override
    public String test1() {
        return testDao.test();
    }
    ```


## 3. Notice - 注意事项

- 支持 spring boot 版本：1.4.4.RELEASE + 。
- 建议去掉 spring boot 自带的数据源自动加载：`@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })` ，不排除此加载，程序加载并不会引发任何错误，只不过会占用一些系统资源。
  ``` JAVA
    @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
    public class App {
      public static void main(String[] args) {
          SpringApplication.run(App.class, args);
      }
    }
  ```

## 4. ChangeLog - 更新记录

- **0.1.2** [2018-03-16]
    - 对创建数据源部分代码进行了重构优化。
    - 修复 Tomcat 、DBCP 等数据源实现工具配置参数无效问题。

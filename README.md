---
title: SpringBoot整合Swagger
date: 2020-05-13 20:01:51
tags:
	- SpringBoot整合Swagger
categories:
	- SpringBoot
	- Swagger
---

GitHub：https://github.com/pawnbrokers/SwaggerSpringBoot

# 1. Swagger简介

<!-- more -->

## 1.1 背景

**前后端分离**

- 前端 -> 前端控制层、视图层
- 后端 -> 后端控制层、服务层、数据访问层
- 前后端通过API进行交互
- 前后端相对独立且松耦合

**产生的问题**

- 前后端集成，前端或者后端无法做到“及时协商，尽早解决”，最终导致问题集中爆发



## 1.2 解决思路

写**提纲文档，并保证实时更新**

## 1.3  Swagger

- 号称世界上最流行的API框架
- Restful Api 文档在线自动生成器 => **API 文档 与API 定义同步更新**
- 直接运行，在线测试API
- 支持多种语言 （如：Java，PHP等）

![image-20200513200812787](https://i.loli.net/2020/05/13/gKH4apCY9BuvQFo.png)

> 两大作用：
>
> 1. **实时更新的文档**
> 2. **方便进行接口测试**

**注意1**： 加不加Swagger对我们的程序逻辑没有任何影响

**注意2**：项目部署的时候一定要关闭Swagger



# 2. SpringBoot整合Swagger

1. 新建SpringBoot-web项目
2. 引入Swagger依赖

```xml
<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger2 -->
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger2</artifactId>
   <version>2.9.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger-ui -->
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger-ui</artifactId>
   <version>2.9.2</version>
</dependency>
```

3. 编写正常的Controller测试代码

![image-20200513201215064](https://i.loli.net/2020/05/13/dDopylb9PkuwQXq.png)

4. 要使用Swagger，我们要使用一个配置类来配置Swagger

```java
@Configuration //配置类
@EnableSwagger2// 开启Swagger2的自动配置
public class SwaggerConfig {  
}
```

这个时候我们就已经可以使用Swagger了，因为他有一些默认配置

5. 访问测试 ：http://localhost:8080/swagger-ui.html ，可以看到swagger的界面；

![image-20200513201318032](https://i.loli.net/2020/05/13/Gmk3zXsVuvKNn7L.png)

可以看到有四个模块组成：也就是我们主要配置的四个部分：

1. ApiInfo 主要是一些介绍信息
2. 右上角的分组信息，不同人负责不同的模块
3. 中间的请求信息

> 这里我们只写了一个Controller，可以看到springboot是**自带一个errorController的接口**。

4. Models主要是存储我们的ModelAndView以及实体类等

![image-20200513201613284](https://i.loli.net/2020/05/13/78b9YWfPajtvVmM.png)



# 3. Swagger配置类

## 3.1 Docket

Swagger实例Bean是Docket，所以通过配置Docket实例来配置Swaggger。

```java
@Bean //配置docket以配置Swagger具体参数
public Docket docket() {
   return new Docket(DocumentationType.SWAGGER_2);
}
```



## 3.2 ApiInfo

可以通过apiInfo()属性配置文档信息，也就是左上角的说明信息

```java
/配置文档信息
private ApiInfo apiInfo() {
   Contact contact = new Contact("联系人名字", "http://xxx.xxx.com/联系人访问链接","联系人邮箱");
   return new ApiInfo(
           "Swagger学习", // 标题
           "学习演示如何配置Swagger", // 描述
           "v1.0", // 版本
           "http://terms.service.url/组织链接", // 组织链接
           contact, // 联系人信息
           "Apach 2.0 许可", // 许可
           "许可链接", // 许可连接
           new ArrayList<>()// 扩展
  );
}
```

```java
 //配置swagger文档信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("袁堂波", "http://pawnbrokers.github.io", "yuantb@yeah.net");
        return new ApiInfo("袁堂波的SwaggerApi文档",
                "节能主义",
                "1.0",
                "http://pawnbrokers.github.io",
                contact, "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());

    }
```

开启上述个人信息后，界面如下：

![image-20200513202030177](https://i.loli.net/2020/05/13/2znwxjIuyFJEkOU.png)



## 3.1 Docket配置

查看源码发现DocketBean可传的参数如下：

```java
public Docket(DocumentationType documentationType) {
        this.apiInfo = ApiInfo.DEFAULT;
        this.groupName = "default";
        this.enabled = true;
        this.genericsNamingStrategy = new DefaultGenericTypeNamingStrategy();
        this.applyDefaultResponseMessages = true;
        this.host = "";
        this.pathMapping = Optional.absent();
        this.apiSelector = ApiSelector.DEFAULT;
        this.enableUrlTemplating = false;
        this.vendorExtensions = Lists.newArrayList();
        this.documentationType = documentationType;
    }
```



### 3.1.1 加上ApiInfo

```java
@Bean
public Docket docket() {
   return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
}
```



### 3.1.2 配置扫描接口

1. 构建Docket时通过select()方法配置怎么扫描接口。

```java
@Bean
public Docket docket() {
   return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
      .apis(RequestHandlerSelectors.basePackage("com.yuan.controller"))
      .build();
}
```

2. 重启项目测试，由于我们配置根据包的路径扫描接口，所以我们只能看到一个类

3. 除了通过包路径配置扫描接口外，还可以通过配置其他方式扫描接口，这里注释一下所有的配置方式：

   ```java
   any() // 扫描所有，项目中的所有接口都会被扫描到
   none() // 不扫描接口
   // 通过方法上的注解扫描，如withMethodAnnotation(GetMapping.class)只扫描get请求
   withMethodAnnotation(final Class<? extends Annotation> annotation)
   // 通过类上的注解扫描，如.withClassAnnotation(Controller.class)只扫描有controller注解的类中的接口
   withClassAnnotation(final Class<? extends Annotation> annotation)
   basePackage(final String basePackage) // 根据包路径扫描接口
   ```

4. 除此之外，我们还可以配置接口扫描过滤：

```java
@Bean
public Docket docket() {
   return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
      .apis(RequestHandlerSelectors.basePackage("com.kuang.swagger.controller"))
       // 配置如何通过path过滤,即这里只扫描请求以/kuang开头的接口
      .paths(PathSelectors.ant("/yuan/**"))
      .build();
}
```

5. PathSelectors的可选值如下：

```java
any() // 任何请求都扫描
none() // 任何请求都不扫描
regex(final String pathRegex) // 通过正则表达式控制
ant(final String antPattern) // 通过ant()控制
```



### 3.1.3 配置Swagger开关

我们可以通过Docket.enable()传入一个布尔量的方式，来开闭Swagger（建造者模式）

方法有两种：

1. 运行时读取环境

```java

        /**
         * 获取项目的环境
         * 需要传入environment
         * 设置要显示的swagger环境
         * 通过eenvironmentacceptsProfiles(profiles)判断所处的环境
         * */
        Profiles profiles = Profiles.of("dev", "test");
        boolean b = environment.acceptsProfiles(profiles);
```

```java
@Bean
public Docket docket(Environment environment) {
   // 设置要显示swagger的环境
   Profiles of = Profiles.of("dev", "test");
   // 判断当前是否处于该环境
   // 通过 enable() 接收此参数判断是否要显示
   boolean b = environment.acceptsProfiles(of);
   
   return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .enable(b) //配置是否启用Swagger，如果是false，在浏览器将无法访问
      .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
      .apis(RequestHandlerSelectors.basePackage("com.kuang.swagger.controller"))
       // 配置如何通过path过滤,即这里只扫描请求以/kuang开头的接口
      .paths(PathSelectors.ant("/kuang/**"))
      .build();
}
```



2. 读取yaml配置文件中的值，得到一个布尔量

```java
    @Value("${spring.profiles.active}")
    String active;
```

![image-20200513203239944](https://i.loli.net/2020/05/13/gG34OrDyQJCTbsm.png)

![image-20200513203251057](https://i.loli.net/2020/05/13/O6mRuAUHZ71LYzj.png)





**我们可以创造多个Docket，赋予多个分组**





# 4. 常用注解

## 4.1 实体配置

1. 新建实体类

```java
@ApiModel("用户实体")
public class User {
   @ApiModelProperty("用户名")
   public String username;
   @ApiModelProperty("密码")
   public String password;
}
```

2. 只要这个实体在**请求接口**的返回值上（即使是泛型），都能映射到实体项中：

```java
 @PostMapping("/user")
    public User user(){
        return new User();
    }
```

3. 测试

![image-20200513203635802](https://i.loli.net/2020/05/13/SwmO56C8R231DVu.png)

注：**并不是因为@ApiModel这个注解让实体显示在这里了**，而是只要出现在接口方法的返回值上的实体都会显示在这里，而@ApiModel和@ApiModelProperty这两个注解只是为实体添加注释的。

@ApiModel为类添加注释

@ApiModelProperty为类属性添加注释

## 4.2 其他注解

| Swagger注解                                                  | 简单说明                                             |
| ------------------------------------------------------------ | ---------------------------------------------------- |
| @Api(tags = "xxx模块说明")                                   | 作用在模块类上                                       |
| @ApiOperation("xxx接口说明")                                 | 作用在接口方法上                                     |
| @ApiModel("xxxPOJO说明")                                     | 作用在模型类上：如VO、BO                             |
| @ApiModelProperty(value = "xxx属性说明",hidden = true)       | 作用在类方法和属性上，hidden设置为true可以隐藏该属性 |
| @ApiParam("xxx参数说明")作用在参数、方法和字段上，类似@ApiModelProperty |                                                      |



# 5. 测试

## 5.1 普通无参请求

![image-20200513203945465](https://i.loli.net/2020/05/13/3QCnYbBNu5rA7Pe.png)

![image-20200513203959125](https://i.loli.net/2020/05/13/C1bR2Fn47l5NMa9.png)

![image-20200513204018515](https://i.loli.net/2020/05/13/5jCoMGbdwlhf9pR.png)

因为是无参的，所以测试结果返回200，响应体为“hello”



## 5.2 有参请求

![image-20200513204102777](https://i.loli.net/2020/05/13/B3YWOqwVmPu9MAZ.png)

![image-20200513204126275](https://i.loli.net/2020/05/13/Yn4T5zaBp7CuLK2.png)

![image-20200513204133933](https://i.loli.net/2020/05/13/G5igqJHIokX8Qb2.png)
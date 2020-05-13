package com.yuan.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

//开启swagger2
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Value("${spring.profiles.active}")
    String active;


    //配置类swagger的docket的bean实例
    @Bean
    public Docket docket(Environment environment) {


        /**
         * 获取项目的环境
         * 需要传入environment
         * 设置要显示的swagger环境
         * 通过eenvironmentacceptsProfiles(profiles)判断所处的环境
         * */
        Profiles profiles = Profiles.of("dev", "test");
        boolean b = environment.acceptsProfiles(profiles);


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("yuan")
                .select()
                //配置要扫描的包
                //basepackage
                //withClassAnnotation: 扫描类上的注解
                //withMethodAnnotation:扫描方法上的注解
//                .apis(RequestHandlerSelectors.withClassAnnotation(GetMapping.class))
                .apis(RequestHandlerSelectors.basePackage("com.yuan"))
                //path() 过滤什么路径
//                .paths(PathSelectors.ant("/yuan/**"))
                .build()
                //配置是否自动启动swagger,false则swagger不能再浏览器中访问
                .enable(active.equals("dev"))
                ;
    }


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


}

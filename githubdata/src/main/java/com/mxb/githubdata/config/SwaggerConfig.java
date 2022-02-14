package com.mxb.githubdata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    public static final String API_V1 = "/api/v1/githubdata";

    @Value("${githubdata.swagger.enable:true}")
    private boolean enable;

    @Bean
    public Docket retrievalWebDocs() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable)
                .groupName("com/mxb/githubdata")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mxb.githubdata.userinterface.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GitHub开发者数据获取与管理系统API文档")
                .description("Github Data Manager RESTful API Document Powered by xbmeng")
                .version("1.0.0")
                .build();
    }
}


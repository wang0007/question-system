package com.neuq.question.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author liuhaoi
 */
@Configuration
@Profile({"test", "local", "dev", "develop"})
@Import({SpringDataRestConfiguration.class, BeanValidatorPluginsConfiguration.class})
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket mobileAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(mobileAPIInfo())
                .groupName("REST API")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/rest/.*"))
                .build();
    }

    private ApiInfo mobileAPIInfo() {
        return new ApiInfoBuilder()
                .title("会务系统接口文档")
                .description("会务系统接口文档")
                .contact(new Contact("刘豪", "", "liuhaoi@yonyou.com"))
                .version("1.0.1")
                .build();
    }


}

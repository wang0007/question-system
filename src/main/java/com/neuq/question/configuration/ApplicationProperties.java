package com.neuq.question.configuration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author liuhaoi
 */
@Data
@Configuration
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties("yonyoucloud.upesn.conference")
@Validated
public class ApplicationProperties extends AbstractApplicationProperties {

    /**
     * 当前服务地址
     */
    @Valid
    @NotNull
    private String host;

    /**
     * 页面前缀
     */
    @Valid
    @NotNull
    private String webPrefix;

    /**
     * 应用前缀
     */
    @Valid
    @NotNull
    private String appPrefix;

    /**
     * 会务的应用ID
     */
    private String appId;




}

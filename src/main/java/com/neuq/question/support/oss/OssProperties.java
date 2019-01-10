package com.neuq.question.support.oss;

/**
 * @author wangshyi
 * @date 2018/12/29  10:46
 */

import com.neuq.question.configuration.AbstractApplicationProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author liuhaoi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "yonyoucloud.upesn.file.oss")
@Validated
public class OssProperties extends AbstractApplicationProperties {


    @NotNull
    @Valid
    private String downloadUrlPrefix;

    @NotNull
    @Valid
    private String bucketName;

    @NotNull
    @Valid
    private String internalEndpoint;

    @NotNull
    @Valid
    private String accessKey;

    @NotNull
    @Valid
    private String accessSecret;

    /**
     * 下载时,http头里面的过期时间
     */
    private Long expirationDelaySecond = 60L * 60 * 24 * 365 * 100;

    @NestedConfigurationProperty
    private GenericObjectPoolConfig pool;

}


package com.neuq.question.service.iapi;

/**
 * @author wangshyi
 * @date 2019/1/11  16:09
 */

import com.neuq.question.service.iapi.pojo.AbstractApplicationProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author liuhaoi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "yonyoucloud.upesn.inapi")
@Component
public class InAPIProperties extends AbstractApplicationProperties {

    private final String host = "http://localhost:8080";

    @NotNull
    @Valid
    private String salt;

    private String agentId;

    @NotNull
    @Valid
    private String version;

    private Integer connectTimeoutMs = 30 * 1000;

    private Integer readTimeoutMs = 120 * 1000;

    @NestedConfigurationProperty
    private CacheConfig cacheConfig = new CacheConfig();

    private static class CacheConfig {

        /**
         * 用户缓存时间, 单位秒, -1表示不缓存
         */
        private Integer user = -1;

        /**
         * 组织缓存时间, 单位秒, -1表示不缓存
         */
        private Integer org = -1;
    }


}

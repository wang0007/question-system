package com.neuq.question.support.oss;

/**
 * @author wangshyi
 * @date 2018/12/29  10:46
 */


import com.neuq.question.support.oss.domain.FileStorageServiceOSSImpl;
import com.neuq.question.support.oss.domain.OSSClientManager;
import com.neuq.question.support.oss.start.ApplicationDirectory;
import com.neuq.question.support.oss.start.FileRemotePathPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuhaoi
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(UpesnFileOssAutoConfiguration.CONDITION_PROPERTY)
@RequiredArgsConstructor
public class UpesnFileOssAutoConfiguration {

    public static final String CONDITION_PROPERTY = "yonyoucloud.upesn.file.oss.bucket-name";

    private final OssProperties ossProperties;

    @Bean
    @ConditionalOnMissingBean
    public OSSClientManager ossClientManager() {
        return new OSSClientManager(ossProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @Qualifier("oss")
    public FileStorageServiceOSSImpl ossFileStorageService(OSSClientManager ossClientManager,
                                                           ApplicationDirectory applicationDirectory,
                                                           FileRemotePathPolicy fileNamePolicy) {
        return new FileStorageServiceOSSImpl(ossProperties, ossClientManager, fileNamePolicy, applicationDirectory);
    }

}

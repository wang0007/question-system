package com.neuq.question.support.oss.domain;

import com.aliyun.oss.OSSClient;
import com.neuq.question.support.oss.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author wangshyi
 * @date 2018/12/29  10:45
 */
@Slf4j
public class OSSClientManager {

    private final OssProperties ossProperties;

    private GenericObjectPool<OSSClient> pool;

    public OSSClientManager(OssProperties ossProperties) {
        this.ossProperties = ossProperties;

        GenericObjectPoolConfig config = getConfig();
        OSSClientFactory ossClientFactory = new OSSClientFactory();
        pool = new GenericObjectPool<>(ossClientFactory, config);
    }

    /***
     * 从池中借用OSSClient
     *
     * @return OSSClient
     */
    public OSSClient getOSSClient() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            log.error("borrow OSSClient form common pool error ", e);
            return null;
        }
    }

    /***
     * 将OSSClient对象归还到池中
     *
     * @param ossClient OSSClient对象
     */
    public void releaseOSSClient(OSSClient ossClient) {
        pool.returnObject(ossClient);
    }

    private GenericObjectPoolConfig getConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        /** 池中最大的连接数 **/
        config.setMaxTotal(100);
        /** 最大连接超时时间 **/
        config.setMaxWaitMillis(3000);
        /** 最大空闲连接数 **/
        config.setMaxIdle(10);
        /** 空闲连接检测线程，检测周期 **/
        config.setTimeBetweenEvictionRunsMillis(120000);
        return config;
    }

    private class OSSClientFactory extends BasePooledObjectFactory<OSSClient> {

        @Override
        public OSSClient create() {

            String internalEndpoint = ossProperties.getInternalEndpoint();
            String accessKeyId = ossProperties.getAccessKey();
            String accessKeySecret = ossProperties.getAccessSecret();

            log.info("create oss client with endpoint {}", internalEndpoint);

            // 创建OSSClient实例
            return new OSSClient(internalEndpoint, accessKeyId, accessKeySecret);
        }

        @Override
        public PooledObject<OSSClient> wrap(OSSClient obj) {
            return new DefaultPooledObject<>(obj);
        }

    }

}

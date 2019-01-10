package com.neuq.question.support.file;

import org.slf4j.event.Level;

import java.lang.annotation.*;

/**
 * @author wangshyi
 * @date 2018/12/27  14:17
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RecordTime {

    /**
     * @return 输出是否打印参数
     */
    boolean withArgs() default false;

    /**
     * 是否使用debug级别输出参数, 不允许debug时, 基本不耗费性能; 当withArgs开启时,本配置不生效
     *
     * @return 是否使用debug级别输出参数
     */
    boolean debugArgs() default false;

    /**
     * 打印时间时用的默认日志级别, 注意即使设置为ERROR, 真正的级别也是WARN, 但是会发送微信预警
     *
     * @return 日志级别
     */
    Level defaultLogLevel() default Level.DEBUG;

    /**
     * 超过阈值的会打印警告日志
     *
     * @return 阈值
     */
    int warnThresholdMs() default 1000;

    /**
     * 超过阈值的会打印错误日志, 和starter-alarm联合使用会触发微信预警,
     * 比warnThresholdMs有更高的优先级, 如果方法执行时间大于warnThresholdMs, 并且大于errorThresholdMs, 则只打印错误日志
     *
     * @return 阈值
     */
    int errorThresholdMs() default 30000;


}


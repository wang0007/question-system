package com.neuq.question.support.oss.start;

import java.io.File;

/**
 * @author wangshyi
 * @date 2018/12/29  11:02
 */
public interface ApplicationDirectory {

    /**
     * 获取应用的Home目录绝对路径
     *
     * @return 应用的Home目录绝对路径
     */
    String getHomeDirectoryPath();

    /**
     * 获取应用的Home目录
     *
     * @return 应用的Home目录
     */
    File getHomeDirectory();

    /**
     * 获取临时文件目录路径, 默认home目录下temp文件夹, 如果已经存在temp文件或者目录不可写, 名称更改为`temp_[13位时间戳]`
     *
     * @return 临时文件目录路径
     */
    String getTempDirectoryPath();

    /**
     * 获取临时文件目录, 默认home目录下temp文件夹, 如果已经存在temp文件或者目录不可写, 名称更改为`temp_[13位时间戳]`
     *
     * @return 临时文件目录
     */
    File getTempDirectory();

    /**
     * 获取临时文件目录下子目录路径, 不存在则新建, 存在但不可写, 抛出异常
     *
     * @param subDirectoryName 子目录名称
     * @return 临时文件目录下子目录路径
     */
    String getTempDirectoryPath(String subDirectoryName);

    /**
     * 获取临时文件目录下子目录, 不存在则新建, 存在但不可写, 抛出异常
     *
     * @param subDirectoryName 子目录名称
     * @return 临时文件目录下子目录
     */
    File getTempDirectory(String subDirectoryName);
}


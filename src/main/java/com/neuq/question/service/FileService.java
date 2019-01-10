package com.neuq.question.service;

/**
 * @author yegk7
 * @since 2018/8/11 14:59
 */
public interface FileService {

    /**
     * 构建文件名
     *
     * @param conferenceId 大会ID
     * @param activityId   活动ID
     * @param fileName     文件名部分字段
     * @return 文件名
     */
    StringBuilder buildFileName(String conferenceId, String activityId, String fileName);

    /**
     * 构建文件名
     *
     * @param conferenceId 大会ID
     * @param fileName     文件名部分字段
     * @return 文件名
     */
    StringBuilder buildFileName(String conferenceId, String fileName);
}

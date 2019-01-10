package com.neuq.question.service;


import com.neuq.question.web.rest.pojo.NotSignInUserResult;

import java.util.List;

/**
 * @author yegk7
 * @since 2018/7/19 20:18
 */
public interface ActivitySignInRecordService {

    /**
     * 将已签到的list<>转换为list<list<Obj>>
     *
     * @param activityId   活动ID
     * @param conferenceId 大会ID
     * @return list<list < Obj>>
     */
    List<List<Object>> getSignInDataList(String activityId, String conferenceId);

    /**
     * 将未签到的list<>转换为list<list<Obj>>
     *
     * @param activityId   活动ID
     * @param conferenceId 大会ID
     * @return list<list < Obj>>
     */
    List<List<Object>> getNotSignInDataList(String activityId, String conferenceId);

    /**
     * 获取未签到人员列表
     *
     * @param activityId   活动ID
     * @param conferenceId 大会ID
     * @return 未签到人员列表
     */
    List<NotSignInUserResult> getNotSignInUserList(String activityId, String conferenceId);
}

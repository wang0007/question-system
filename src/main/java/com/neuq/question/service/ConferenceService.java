package com.neuq.question.service;

import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.web.rest.management.conference.ConferenceController;

import java.util.List;

/**
 * @author wangshyi
 */
public interface ConferenceService {

    /**
     * 获取并校验大会是否存在
     *
     * @param conferenceId 大会ID
     * @return 大会
     */
    ConferenceDO findAndVerifyConference(String conferenceId);

    /**
     * 创建大会
     *
     * @param creator    大会创建人memberId
     * @param conference 大会信息
     * @return 大会
     */
    ConferenceDO createConference(String creator, ConferenceController.ConferenceDTO conference);

    /**
     * 将大会列表按照进行中、未开始、已结束进行排序
     *
     * @param list 大会列表
     * @return 排序后的列表
     */
    List<ConferenceDO> sortConferenceList(List<ConferenceDO> list);
}

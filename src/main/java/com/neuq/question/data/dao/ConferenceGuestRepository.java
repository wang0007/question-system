package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ConferenceGuestDO;
import com.neuq.question.web.rest.management.conference.ConferenceGuestController;

import java.util.List;

/**
 * @author yegk7
 * @since 2018/8/1 19:02
 */
public interface ConferenceGuestRepository {

    /**
     * 添加或者更新参会人员
     *
     * @param conferenceGuestDO 参会人员信息
     * @param conferenceId      大会ID
     */
    void save(ConferenceGuestDO conferenceGuestDO, String conferenceId);

    /**
     * 添加或者更新参会人员
     *
     * @param conferenceGuestDO 参会人员信息
     * @param conferenceId      大会ID
     */
    void upsert(ConferenceGuestDO conferenceGuestDO, String conferenceId);

    /**
     * 获取大会参会人员总数
     *
     * @param conferenceId 大会ID
     * @return 参会人员数
     */
    long count(String conferenceId);

    /**
     * 获取参会人员列表
     *
     * @param conferenceId 大会ID
     * @param start        起始页
     * @param size         大小
     * @return 参会人员列表
     */
    List<ConferenceGuestDO> list(String conferenceId, int start, int size);

    /**
     * 获取参会人员手机号列表
     *
     * @param conferenceId 大会ID
     * @return 手机号列表
     */
    List<String> mobileList(String conferenceId);

    /**
     * 修改参会人员信息
     *
     * @param guestDTO     参会人员信息
     * @param guestId      参会人ID
     * @param conferenceId 大会ID
     * @return 更新成功条数
     */
    long update(ConferenceGuestController.ConferenceParticipantsDTO guestDTO, String guestId, String conferenceId);

    /**
     * 删除参会人员信息
     *
     * @param guestId      参会人ID
     * @param conferenceId 大会ID
     */
    void delete(String guestId, String conferenceId);

    /**
     * 用户是否为参会人员
     *
     * @param conferenceId 大会ID
     * @param mobile       手机号
     * @return 是否为参会人员
     */
    boolean isGuest(String conferenceId, String mobile);
}

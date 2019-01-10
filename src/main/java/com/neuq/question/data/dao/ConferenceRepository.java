package com.neuq.question.data.dao;

import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.web.rest.management.conference.ConferenceController;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 会议数据操作
 *
 * @author liuhaoi
 */
public interface ConferenceRepository {

    /**
     * 列取所有大会
     *
     * @param endTimeAfter 还没有结束的大会
     * @param size         分页大小,limit
     * @param start        skip
     * @return 大会列表
     */
    List<ConferenceDO> list(Date endTimeAfter, int start, int size);

    /**
     * 查找排除未开始的大会列表
     *
     * @return 大会列表
     */
    List<ConferenceDO> listWithoutNoStart();

    /**
     * 总数量
     *
     * @param endTimeAfter 结束时间在某个时间之后
     * @return 总数量
     */
    long count( Date endTimeAfter);

    /**
     * 空间下某人负责的大会数
     *
     * @param memberId     人员ID
     * @param endTimeAfter 结束时间
     * @return 大会数量
     */
    long countByMemberId( String memberId, Date endTimeAfter);

    /**
     * 根据大会ID查询
     *
     * @param conferenceId 大会ID
     * @return 大会实体
     */
    ConferenceDO queryByConferenceId(String conferenceId);

    /**
     * 保存一条新的记录,只插入不更新
     *
     * @param conference 大会
     */
    void save(ConferenceDO conference);

    /**
     * 更新大会
     *
     * @param conferenceId 大会ID
     * @param conference   大会
     * @return 更改的条数, 如更改应该为1
     */
    long update(String conferenceId, ConferenceController.ConferenceDTO conference);

    /**
     * 禁用大会
     *
     * @param conferenceId 大会ID
     * @return 更改的条数, 如更改应该为1
     */
    long disable(String conferenceId);

    /**
     * 查询某人是大会管理员
     *
     * @param memberId 人员ID
     * @return 大会列表
     */
    List<ConferenceDO> queryByMemberId( String memberId);

    /**
     * 查询某人负责的大会列表，包含分页信息
     *
     * @param memberId     成员ID
     * @param endTimeAfter 截止时间
     * @param start        起始
     * @param size         大小
     * @return 负责的大会列表
     */
    List<ConferenceDO> queryByMemberIdWithPage(String memberId, Date endTimeAfter, int start, int size);

    /**
     * 查询空间内所有大会
     * @return 大会列表
     */
    List<ConferenceDO> queryAllConference();

    /**
     * 查询大会的帮他签到人员的memberId列表
     *
     * @param conferenceId 大会ID
     * @return 帮他签到人员memberId列表
     */
    Set<String> queryHelpSignInMembers(String conferenceId);

}

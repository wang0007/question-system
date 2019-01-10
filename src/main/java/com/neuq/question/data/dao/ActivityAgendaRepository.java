package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.ActivityAgendaDO;

/**
 * @author yegk
 */
public interface ActivityAgendaRepository {

    /**
     * 活动的议程
     *
     * @param activityId 活动ID
     * @return 议程详情
     */
    ActivityAgendaDO queryById(String activityId);

    /**
     * 更新区分上下午状态
     *
     * @param activityId    活动ID
     * @param withAmPmTitle 是否区分上下午
     * @return 更改条数
     */
    long updateWithAmPmTitle(String activityId, boolean withAmPmTitle);

    /**
     * 添加单个活动议程
     *
     * @param activityId 活动id
     * @param agenda     议程
     * @return 成功添加数量
     */
    long saveAgendaItem(String activityId, ActivityAgendaDO.Agenda agenda);

    /**
     * 添加活动议程
     *
     * @param agendaDO 活动议程
     */
    void save(ActivityAgendaDO agendaDO);

    /**
     * 更新活动议程
     *
     * @param activityId 活动ID
     * @param itemId     议程对应id
     * @param agenda     议程内容
     * @return 更新条数
     */
    long updateAgendaItem(String activityId, String itemId, ActivityAgendaDO.Agenda agenda);

    /**
     * 删除活动议程
     *
     * @param activityId 活动id
     * @param itemId     议程id
     * @return 删除数量
     */
    long deleteAgendaItem(String activityId, String itemId);
}

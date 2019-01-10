package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.ConferenceGuideDO;

import java.util.Collection;
import java.util.List;

/**
 * @author liuhaoi
 */
public interface ConferenceGuideRepository {

    List<ConferenceGuideDO> list(String conferenceId, int skip, int limit);

    long count(String conferenceId);

    ConferenceGuideDO queryById(String conferenceId, String guideId);

    void save(ConferenceGuideDO guide);

    void saveAll(Collection<ConferenceGuideDO> guides);

    long saveGuideItem(String conferenceId, String guideId, ConferenceGuideDO.GuideItem item);

    long updateName(String conferenceId, String guideId, String newName, String icon);

    long updateGuideItem(String conferenceId, String guideId, String itemId, ConferenceGuideDO.GuideItem item);

    long delete(String conferenceId, String guideId);

    long deleteItem(String conferenceId, String guideId, String itemId);

}

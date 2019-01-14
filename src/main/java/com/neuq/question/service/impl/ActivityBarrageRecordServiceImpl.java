package com.neuq.question.service.impl;

import com.neuq.question.data.dao.ActivityBarrageRecordRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ActivityBarrageRecordDO;
import com.neuq.question.data.pojo.ActivityBarrageSettingDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.service.ActivityBarrageRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/11/29  15:53
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityBarrageRecordServiceImpl implements ActivityBarrageRecordService {

    private final ActivityBarrageRecordRepository barrageRepository;


    @Override
    public List<ActivityBarrageRecordDO> queryBarrage(String conferenceId, List<Integer> partitions, Long ts) {
        long max = Math.max(System.currentTimeMillis() - 24L * 60 * 60 * 1000, ts);

        return barrageRepository.queryBarrage(conferenceId, partitions, max);
    }

    @Override
    public void saveBarrage(String conferenceId, String content, InAPIUser user) {

        ActivityBarrageRecordDO barrageDo = new ActivityBarrageRecordDO();
        //生成弹幕的分区
        int partition = (int) (Math.random() * ActivityBarrageSettingDO.FIELD_PARTITION);
        barrageDo.setPartition(partition);
        //设置弹幕删除标记
        barrageDo.setDeleted(false);
        barrageDo.setConferenceId(conferenceId);
        barrageDo.setContent(content);
        barrageDo.setUser(user);
        //弹幕存库
        barrageRepository.saveBarrage(barrageDo);
    }
}

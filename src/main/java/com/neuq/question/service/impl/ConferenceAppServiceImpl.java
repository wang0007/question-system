package com.neuq.question.service.impl;


import com.neuq.question.data.dao.ConferenceAppRepository;
import com.neuq.question.data.pojo.ConferenceAppDO;
import com.neuq.question.service.ConferenceAppService;
import com.neuq.question.support.LocaleUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author liuhaoi
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConferenceAppServiceImpl implements ConferenceAppService {

    private final ConferenceAppRepository appRepository;

    @Override
    public ConferenceAppDO queryConferenceAppWithDefault(String conferenceId) {

        ConferenceAppDO conferenceAppDO = appRepository.queryByConferenceId(conferenceId);

        if(conferenceAppDO == null){
            return new ConferenceAppDO();
        }

        List<ConferenceAppDO.App> appListWithI18n = conferenceAppDO.getApps()
                .stream()
                .peek(app -> {
                    if (app.getI18nName() != null && app.getI18nName().get(LocaleUtil.getLocale()) != null) {
                        app.setName(app.getI18nName().get(LocaleUtil.getLocale()));
                    }
                })
                .collect(Collectors.toList());

        conferenceAppDO.setApps(appListWithI18n);

        return conferenceAppDO;
    }


}

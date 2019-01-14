package com.neuq.question.web.rest.management.feed;

import com.neuq.question.data.dao.ActivityNewsFeedSettingRepository;
import com.neuq.question.data.pojo.ActivityNewsFeedSettingDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author yegk7
 * @date 2018/8/30 10:44
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/activity/{activityId}/feed/setting")
@RequiredArgsConstructor
@Api(value = "发言上墙设置", description = "发言上墙设置")
public class NewsFeedSettingController {

    private final ActivityNewsFeedSettingRepository settingRepository;

    /**
     * 更新发言上墙设置
     *
     * @param activityId 活动ID
     * @param settingDTO 背景图设置
     * @return 背景图
     */
    @ApiOperation(value = "更新发言上墙设置", notes = "更新发言上墙设置")
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public NewsFeedProjectionSettingDTO update(@PathVariable("activityId") String activityId,
                                               @RequestBody NewsFeedProjectionSettingDTO settingDTO) {

        settingRepository.updateProjection(activityId, settingDTO);
        return settingDTO;
    }

    /**
     * 获取发言上墙设置
     *
     * @param activityId 活动ID
     * @return 发言上墙设置
     */
    @ApiOperation(value = "获取发言上墙设置", notes = "获取发言上墙设置")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ActivityNewsFeedSettingDO getSetting(@PathVariable("activityId") String activityId) {

        return settingRepository.queryByActivityId(activityId);
    }


    @Data
    public static class NewsFeedProjectionSettingDTO {
        /**
         * 背景图片地址
         */
        private String background;

        /**
         * 背景图片缩略图
         */
        private String thumbBackground;
    }

}

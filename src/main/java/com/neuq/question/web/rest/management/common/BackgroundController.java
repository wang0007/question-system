package com.neuq.question.web.rest.management.common;

import com.neuq.question.data.dao.ConferenceBackgroundRepository;

import com.neuq.question.data.pojo.common.ConferenceBackgroundDO;
import com.neuq.question.domain.enums.BackgroundType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 背景图片相关接口
 *
 * @author wangshyi
 * @since 2018/11/3 20:45
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/background")
@RequiredArgsConstructor
@Api(value = "背景图片相关接口", description = "背景图片相关接口")
public class BackgroundController {


    private final ConferenceBackgroundRepository conferenceBackgroundRepository;

    /**
     * 插入背景图
     *
     * @param conferenceId            大会ID
     * @param conferenceBackgroundDTO 大会背景图
     * @return 背景图信息
     */
    @ApiOperation(value = "插入背景图,多图添加", notes = "插入背景图")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceBackgroundDO insert(@PathVariable("conferenceId") String conferenceId,
                                         @RequestBody ConferenceBackgroundDTO conferenceBackgroundDTO) {

        ConferenceBackgroundDO conferenceBackgroundDO = new ConferenceBackgroundDO();

        conferenceBackgroundDO.setBackground(conferenceBackgroundDTO.getBackground());
        conferenceBackgroundDO.setBackgroundType(conferenceBackgroundDTO.getBackgroundType());
        setConferenceBackground(conferenceId, conferenceBackgroundDO);

        conferenceBackgroundRepository.insert(conferenceBackgroundDO);

        return conferenceBackgroundDO;
    }


    /**
     * 插入一张背景图
     *
     * @param conferenceId   大会ID
     * @param backgroundBO   背景图
     * @param backgroundType 背景图类型
     * @return 背景图
     */
    @ApiOperation(value = "插入一张背景图，上传完背景图片后掉用", notes = "插入一张背景图")
    @PatchMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceBackgroundDO.BackgroundBO insertBackground(@PathVariable("conferenceId") String conferenceId,
                                                                @RequestBody ConferenceBackgroundDO.BackgroundBO backgroundBO,
                                                                @RequestParam("backgroundType") BackgroundType backgroundType) {

        conferenceBackgroundRepository.insertBackgroundItem(conferenceId, backgroundBO, backgroundType);
        return backgroundBO;
    }

    /**
     * 获取所有背景图
     *
     * @param conferenceId   大会ID
     * @param backgroundType 背景图类型
     * @return 背景图信息
     */
    @ApiOperation(value = "获取所有背景图", notes = "获取所有背景图")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ConferenceBackgroundDO.BackgroundBO> getBackground(@PathVariable("conferenceId") String conferenceId,
                                                                   @RequestParam("backgroundType") BackgroundType backgroundType) {

        ConferenceBackgroundDO conferenceBackgroundDO = conferenceBackgroundRepository.query(conferenceId, backgroundType);

        if (conferenceBackgroundDO == null) {
            conferenceBackgroundDO = new ConferenceBackgroundDO();
            conferenceBackgroundDO.setBackgroundType(backgroundType);
            setConferenceBackground(conferenceId, conferenceBackgroundDO);
            conferenceBackgroundRepository.insert(conferenceBackgroundDO);
        }

        // 加入默认的九张背景图
        List<ConferenceBackgroundDO.BackgroundBO> groundBOList = conferenceBackgroundRepository.getDefaultBackground(backgroundType).getBackground();
        if (conferenceBackgroundDO.getBackground() != null) {
            groundBOList.addAll(conferenceBackgroundDO.getBackground());
        }
        return groundBOList;
    }

    /**
     * 删除指定的背景图
     *
     * @param conferenceId   大会ID
     * @param backgroundId   背景图ID
     * @param backgroundType 背景图类型
     */
    @ApiOperation(value = "删除指定的背景图", notes = "删除指定的背景图")
    @DeleteMapping(value = "/{backgroundId}")
    public void delete(@PathVariable("conferenceId") String conferenceId,
                       @PathVariable("backgroundId") String backgroundId,
                       @RequestParam("backgroundType") BackgroundType backgroundType) {

        conferenceBackgroundRepository.delete(conferenceId, backgroundId, backgroundType);
    }

    /**
     * 设置背景图
     *
     * @param conferenceId           大会ID
     * @param conferenceBackgroundDO 背景图信息
     */
    private void setConferenceBackground(String conferenceId, ConferenceBackgroundDO conferenceBackgroundDO) {

        conferenceBackgroundDO.setConferenceId(conferenceId);
        conferenceBackgroundDO.setId(UUID.randomUUID().toString());
    }

    @Data
    public static class ConferenceBackgroundDTO {

        /**
         * 背景图以及缩略图
         */
        private List<ConferenceBackgroundDO.BackgroundBO> background;

        /**
         * 背景图类型
         */
        private BackgroundType backgroundType;
    }

}

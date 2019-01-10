package com.neuq.question.web.rest.management.conference;


import com.neuq.question.data.dao.ConferenceGuideRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ConferenceGuideDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.domain.enums.OperationType;
import com.neuq.question.service.OperationLogService;
import com.neuq.question.support.LocaleUtil;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 大会指南接口
 *
 * @author liuhaoi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/guide/")
@RequiredArgsConstructor
@Api(value = "大会指南接口", description = "大会指南接口")
public class ConferenceGuideController {

    private final ConferenceGuideRepository repository;

    private final OperationLogService operationLogService;

    private final UserRepsitory userRepsitory;

    private final static String DELETE_GUIDE_ITEM = "delete conference guide item,guideId=%s,guideItemId=%s";

    private final static String UPDATE_GUIDE_ITEM = "update conference guide item,guideId=%s,guideItemId=%s";

    /**
     * 获取大会指南列表
     *
     * @param conferenceId 大会ID
     * @return 指南列表
     */
    @ApiOperation(value = "获取大会指南列表", notes = "获取大会指南列表")
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<ConferenceGuideDO> list(@PathVariable("conferenceId") String conferenceId,
                                              @RequestParam(defaultValue = "0") int start,
                                              @RequestParam(defaultValue = "20") int end) {

        List<ConferenceGuideDO> data = repository.list(conferenceId, start, end);

        long count = repository.count(conferenceId);

        return new PageResult<>(data, count);
    }


    /**
     * 获取大会指南条目
     *
     * @param conferenceId 大会ID
     * @param guideId      指南ID
     * @return 指南条目
     */
    @ApiOperation(value = "获取大会指南条目", notes = "获取大会指南条目")
    @GetMapping(value = "{guideId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ConferenceGuideDO getById(@PathVariable("conferenceId") String conferenceId,
                                     @PathVariable("guideId") String guideId) {
        return repository.queryById(conferenceId, guideId);
    }

    /**
     * 创建大会指南
     *
     * @param conferenceId 大会ID
     * @param guide        指南ID
     * @return 大会指南
     */
    @ApiOperation(value = "创建大会指南", notes = "创建大会指南")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceGuideDO create(@PathVariable("conferenceId") String conferenceId,
                                    @RequestBody ConferenceGuideDTO guide) {

        ConferenceGuideDO dbo = new ConferenceGuideDO();

        dbo.setIcon(guide.getIcon());
        dbo.setConferenceId(conferenceId);
        dbo.setItems(guide.getItems());
        dbo.setName(guide.getName());

        repository.save(dbo);

        return dbo;
    }

    /**
     * 新建大会指南条目
     *
     * @param conferenceId 大会ID
     * @param guideId      指南ID
     * @param item         条目
     * @return 条目
     */
    @ApiOperation(value = "新建大会指南活动", notes = "新建大会指南活动")
    @PostMapping(value = "{guideId}/item", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceGuideDO.GuideItem createItem(@PathVariable("conferenceId") String conferenceId,
                                                  @PathVariable("guideId") String guideId,
                                                  @RequestBody ConferenceGuideDO.GuideItem item) {

        repository.saveGuideItem(conferenceId, guideId, item);

        return item;
    }


    /**
     * 更新指南
     *
     * @param conferenceId    大会ID
     * @param guideId         指南ID
     * @param guideSettingDTO 指南图标和名字信息
     * @return 指南图标和名字信息
     */
    @ApiOperation(value = "更新指南，包括名称和图标", notes = "更新指南")
    @PatchMapping(value = "{guideId}/name")
    public GuideSettingDTO updateName(@PathVariable("conferenceId") String conferenceId,
                                      @RequestParam("memberId") String memberId,
                                      @PathVariable("guideId") String guideId,
                                      @RequestBody GuideSettingDTO guideSettingDTO) {

        repository.updateName(conferenceId, guideId, guideSettingDTO.getName(), guideSettingDTO.getIcon());
        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, System.currentTimeMillis(), "update conference guide,guideId=" + guideId);

        return guideSettingDTO;
    }

    /**
     * 更改指南的活动
     *
     * @param conferenceId 大会ID
     * @param guideId      指南ID
     * @param itemId       条目ID
     * @param item         条目
     * @return 活动
     */
    @ApiOperation(value = "更改指南的活动", notes = "更改指南的活动")
    @PutMapping(value = "{guideId}/item/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ConferenceGuideDO.GuideItem updateItem(@PathVariable("conferenceId") String conferenceId,
                                                  @RequestParam("memberId") String memberId,
                                                  @PathVariable("guideId") String guideId,
                                                  @PathVariable("itemId") String itemId,
                                                  @RequestBody ConferenceGuideDO.GuideItem item) {

        repository.updateGuideItem(conferenceId, guideId, itemId, item);
        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        operationLogService.createOperationLog(inAPIUser, OperationType.UPDATE, conferenceId,
                null, System.currentTimeMillis(), String.format(UPDATE_GUIDE_ITEM, guideId, itemId));

        return item;
    }

    /**
     * 删除指南
     *
     * @param conferenceId 大会ID
     * @param guideId      指南ID
     */
    @ApiOperation(value = "删除指南", notes = "删除指南")
    @DeleteMapping("{guideId}")
    public void delete(@PathVariable("conferenceId") String conferenceId,
                       @RequestParam("memberId") String memberId,
                       @PathVariable("guideId") String guideId) {
        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        repository.delete(conferenceId, guideId);

        operationLogService.createOperationLog(inAPIUser, OperationType.DELETE, conferenceId,
                null, System.currentTimeMillis(), "delete conference guide,guideId=" + guideId);

    }

    /**
     * 删除活动
     *
     * @param conferenceId 大会ID
     * @param guideId      指南ID
     * @param itemId       条目ID
     */
    @ApiOperation(value = "删除条目", notes = "删除条目")
    @DeleteMapping("{guideId}/item/{itemId}")
    public void delete(@PathVariable("conferenceId") String conferenceId,
                       @RequestParam("memberId") String memberId,
                       @PathVariable("guideId") String guideId,
                       @PathVariable("itemId") String itemId) {

        repository.deleteItem(conferenceId, guideId, itemId);
        InAPIUser inAPIUser = userRepsitory.queryById(memberId);

        operationLogService.createOperationLog(inAPIUser, OperationType.DELETE, conferenceId,
                null, System.currentTimeMillis(), String.format(DELETE_GUIDE_ITEM, guideId, itemId));

    }

    /**
     * 获取默认的五个指南
     *
     * @param conferenceId 大会ID
     * @return 指南列表
     */
    @ApiOperation(value = "获取默认的五个指南", notes = "获取默认的五个指南")
    @GetMapping("initial/list")
    public List<ConferenceGuideDO> getInitialList(@PathVariable("conferenceId") String conferenceId) {

        return repository.list(LocaleUtil.getDefaultIdWithLocale(), 0, 5);
    }

    @Data
    @Validated
    public static class ConferenceGuideDTO {

        @NotNull
        @Valid
        private String icon;

        @Valid
        @Size(max = 50, min = 1)
        private String name;

        @Valid
        private List<ConferenceGuideDO.GuideItem> items;

    }

    @Data
    @Validated
    public static class GuideSettingDTO {

        @NotNull
        @Valid
        private String icon;

        @Valid
        @Size(max = 50, min = 1)
        private String name;
    }


}

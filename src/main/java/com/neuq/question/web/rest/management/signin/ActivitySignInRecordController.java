package com.neuq.question.web.rest.management.signin;

import com.neuq.question.data.dao.ActivitySignInRecordRepository;

import com.neuq.question.data.dao.ConferenceActivityRepository;
import com.neuq.question.data.pojo.ActivitySignInRecordDO;
import com.neuq.question.data.pojo.ConferenceActivityDO;
import com.neuq.question.service.ActivitySignInRecordService;
import com.neuq.question.web.rest.pojo.IncrementalResult;
import com.neuq.question.web.rest.pojo.NotSignInUserResult;
import com.neuq.question.web.rest.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 签到记录相关接口
 *
 * @author liuhaoi
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/activity/{activityId}/signin/record")
@RequiredArgsConstructor
@Api(value = "签到记录相关接口", description = "签到记录相关接口")
public class ActivitySignInRecordController {

    private final ActivitySignInRecordService activitySignInRecordService;

    private final ActivitySignInRecordRepository signInRecordRepository;

    private final ConferenceActivityRepository conferenceActivityRepository;

    /**
     * 获取签到记录
     *
     * @param activityId 活动ID
     * @param keyword    手机号搜索,支持非完整匹配
     * @return 签到记录列表
     */
    @ApiOperation(value = "获取已签到记录列表", notes = "获取已签到记录")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<ActivitySignInRecordDO> listSigned(@PathVariable("activityId") String activityId,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(defaultValue = "0") int start,
                                                         @RequestParam(defaultValue = "20") int size) {
        String phoneRegex = null;
        String nameRegex = null;
        if (keyword != null) {
            String cleanKeyword = keyword.replaceAll("\\+", "").replaceAll("-", "");
            if (StringUtils.isNumeric(keyword) || StringUtils.isNumeric(cleanKeyword)) {
                phoneRegex = keyword;
            } else {
                nameRegex = keyword;
            }
        }

        List<ActivitySignInRecordDO> signInRecordDOList = signInRecordRepository
                .list(activityId, phoneRegex, nameRegex, start, size);
        Long count = signInRecordRepository.count(activityId, phoneRegex, nameRegex);

        return new PageResult<>(signInRecordDOList, count);
    }


    /**
     * 获取未签到记录
     *
     * @param activityId 活动ID
     * @return 未签到记录列表
     */
    @ApiOperation(value = "获取未签到记录列表", notes = "获取未签到记录")
    @GetMapping(value = "/unsign/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<NotSignInUserResult> listUnSign(@PathVariable("activityId") String activityId,
                                                      @RequestParam(defaultValue = "0") int start,
                                                      @RequestParam(defaultValue = "20") int size) {

        ConferenceActivityDO activityDO = conferenceActivityRepository.query(activityId);
        List<NotSignInUserResult> notSignInUserResultList = activitySignInRecordService.getNotSignInUserList(activityId, activityDO.getConferenceId());

        if (notSignInUserResultList == null) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }

        Long count = (long) notSignInUserResultList.size();

        if (notSignInUserResultList.size() <= start) {
            return new PageResult<>(Collections.emptyList(), count);
        }

        int subSize = Math.toIntExact(size > count - start ? count - start : size);
        return new PageResult<>(notSignInUserResultList.subList(start, start + subSize), count);
    }


    /**
     * 获取实时签到记录
     *
     * @param activityId 活动ID
     * @param ts         时间戳
     * @return 签到记录列表
     */
    @ApiOperation(value = "获取实时签到记录,包含实时签到总人数", notes = "获取实时签到记录，供签到投屏使用，第一次ts传-1")
    @GetMapping(value = "/incremental/{ts}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public IncrementalResult<ActivitySignInRecordDO> getQRCode(@PathVariable("activityId") String activityId,
                                                               @PathVariable("ts") String ts) {
        Long currentTs = System.currentTimeMillis();

        List<ActivitySignInRecordDO> signInRecordDOList = signInRecordRepository
                .incrementalList(activityId, Long.parseLong(ts), currentTs);

        long signCount = signInRecordRepository.count(activityId);

        IncrementalResult<ActivitySignInRecordDO> result = new IncrementalResult<>();
        result.setNextTs(currentTs);
        result.setData(signInRecordDOList);
        result.setCount(signCount);
        return result;
    }

    /**
     * 获取实时签到人数
     *
     * @param activityId 活动ID
     * @param ts         时间戳
     * @return 签到人数
     */
    @ApiOperation(value = "获取实时签到人数", notes = "获取实时签到人数，供签到投屏使用")
    @GetMapping(value = "/incremental/count/{ts}", produces = MediaType.APPLICATION_JSON_VALUE)
    public long getCurrentCount(@PathVariable("activityId") String activityId,
                                @PathVariable("ts") String ts) {

        return signInRecordRepository.count(activityId);
    }
}

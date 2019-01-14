package com.neuq.question.web.rest.client.barrage;


import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.service.ActivityBarrageRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.print.Printer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static net.bytebuddy.implementation.FixedValue.self;

/**
 * @author wangshyi
 * @date 2018/11/30  10:22
 */
@Slf4j
@RestController
@RequestMapping("rest/v1/conference/{conferenceId}/barrage/record")
@RequiredArgsConstructor
@Api(value = "弹幕发送接口", description = "弹幕发送接口")
public class ActivityBarragePushController {

    private final ActivityBarrageRecordService barrageService;

    private final UserRepsitory userRepsitory;

    /**
     * 存储弹幕
     *
     * @param conferenceId 大会id
     * @param content      弹幕内容
     */
    @ApiOperation(value = "存储弹幕", notes = "存储弹幕")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void saveBarrage(@PathVariable("conferenceId") String conferenceId,
                            @RequestParam("memberId") String memberId,
                            @RequestBody BarrageContentDTO content) {

        InAPIUser inAPIUser = userRepsitory.queryById(memberId);
        barrageService.saveBarrage(conferenceId, content.getContent(), inAPIUser);

    }

    @Data
    public static class BarrageContentDTO {
        private String content;
    }


}
